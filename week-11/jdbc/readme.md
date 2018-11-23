```java

@Slf4j
public class DatabaseTemplate extends JdbcTemplate implements AutoCloseable {
    private TransactionOperations transactionOperations;
    private final Consumer<DataSource> destroy;

    public DatabaseTemplate(DataSource dataSource, Consumer<DataSource> destroy) {
        super(dataSource);
        this.destroy = destroy;
    }

    DataSourceTransactionManager getTransactionManager() {
        return (DataSourceTransactionManager) ((TransactionTemplate) transactionOperations).getTransactionManager();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        if (dataSource == null) {
            return;
        }
        super.setDataSource(dataSource);

        if (transactionOperations == null) {
            DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
            dataSourceTransactionManager.afterPropertiesSet();
            TransactionTemplate transactionTemplate = new TransactionTemplate(dataSourceTransactionManager);
            transactionTemplate.afterPropertiesSet();
            transactionOperations = transactionTemplate;
        }

        DataSourceTransactionManager dataSourceTransactionManager = getTransactionManager();
        if (dataSourceTransactionManager != null) {
            dataSourceTransactionManager.setDataSource(dataSource);
        }
    }

    public void executeDdl(String catalog, String... sql) throws DataAccessException {
        batchUpdate(catalog, sql);
    }

    public int[] executeDml(String catalog, String... sql) throws TransactionException, DataAccessException {
        return transactionOperations.execute(status -> batchUpdate(catalog, sql));
    }

    public int[] executeDmlAndRollback(String catalog, String... sql) throws TransactionException, DataAccessException {
        return transactionOperations.execute(status -> {
            int[] rowsAffected = batchUpdate(catalog, sql);
            status.setRollbackOnly();
            return rowsAffected;
        });
    }

    public QueryResultBody query(final String catalog, final String sql) throws DataAccessException {
        return query(catalog, sql, new DefaultSqlRowSetExtractor());
    }

    public <T> T query(final String catalog, final String sql, SqlRowSetExtractor<T> extractor) throws DataAccessException {
        Assert.notNull(extractor, "SqlRowSetExtractor 不能为null");
        return extractor.extractData(queryForRowSet(catalog, sql));
    }

    public SqlRowSet queryForRowSet(final String catalog, final String sql) throws DataAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL newQueryParameter [" + sql + "]");
        }
        final ResultSetExtractor<SqlRowSet> rse = new SqlRowSetResultSetExtractor();
        return query(catalog, sql, rse);
    }

    public <T> List<T> queryForSingleList(String catalog, String sql, Class<T> elementType) throws DataAccessException {
        RowMapper<T> rowMapper = new SingleColumnRowMapper<>(elementType);
        ResultSetExtractor<List<T>> rse = new RowMapperResultSetExtractor<>(rowMapper);
        return query(catalog, sql, rse);
    }

    public <T> List<T> queryForBeanList(String catalog, String sql, Class<T> elementType) throws DataAccessException {
        RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(elementType);
        ResultSetExtractor<List<T>> rse = new RowMapperResultSetExtractor<>(rowMapper);
        return query(catalog, sql, rse);
    }

    public List<MultiQueryResult<QueryResultBody>> multiQuery(final String catalog, final String[] sqls) throws DataAccessException {
        return multiQuery(catalog, sqls, new DefaultSqlRowSetExtractor());
    }

    public <T> List<MultiQueryResult<T>> multiQuery(final String catalog, final String[] sqls, SqlRowSetExtractor<T> extractor) throws DataAccessException {
        Assert.notNull(extractor, "SqlRowSetExtractor 不能为null");
        return multiQueryForRowSet(catalog, sqls).stream().map(it -> {
            if (it.getData() == null) {
                return new MultiQueryResult<T>(it.getSql(), it.getException());
            } else {
                T data = extractor.extractData(it.getData());
                return new MultiQueryResult<T>(it.getSql(), data);
            }
        }).collect(Collectors.toList());
    }

    public List<MultiQueryResult<SqlRowSet>> multiQueryForRowSet(final String catalog, final String[] sqls) throws DataAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL statements newQueryParameter [" + Arrays.toString(sqls) + "]");
        }
        final ResultSetExtractor<SqlRowSet> rse = new SqlRowSetResultSetExtractor();
        return multiQuery(catalog, sqls, rse);
    }

    public <T> List<MultiQueryResult<List<T>>> multiQueryForSingleList(String catalog, String[] sqls, Class<T> elementType) throws DataAccessException {
        RowMapper<T> rowMapper = new SingleColumnRowMapper<>(elementType);
        ResultSetExtractor<List<T>> rse = new RowMapperResultSetExtractor<>(rowMapper);
        return multiQuery(catalog, sqls, rse);
    }

    private <T> List<MultiQueryResult<T>> multiQuery(final String catalog, final String[] sqls, final ResultSetExtractor<T> rse) throws DataAccessException {
        class QueryConnectionCallback implements ConnectionCallback<List<MultiQueryResult<T>>>, SqlProvider {

            @Override
            public List<MultiQueryResult<T>> doInConnection(Connection con) throws SQLException, DataAccessException {
                con.setCatalog(catalog);
                List<MultiQueryResult<T>> list = new ArrayList<>(sqls.length);
                for (String sql : sqls) {
                    Statement stmt = con.createStatement();
                    ResultSet rs = null;
                    try {
                        rs = stmt.executeQuery(sql);
                        System.out.println("=================================================================");
                        System.out.println("type: {}" + rs.getType());
                        if (rs.getType() == 0) {
                            list.add(new MultiQueryResult<>(sql));
                        } else {
                            T data = rse.extractData(rs);
                            list.add(new MultiQueryResult<T>(sql, data));
                        }
                    } catch (SQLException e) {
                        list.add(new MultiQueryResult<T>(sql, e));
                    } finally {
                        JdbcUtils.closeResultSet(rs);
                    }
                }
                return list;
            }

            @Override
            public String getSql() {
                return String.join("", sqls);
            }
        }
        return execute(new QueryConnectionCallback());
    }

    private <T> T query(final String catalog, final String sql, final ResultSetExtractor<T> rse) throws DataAccessException {
        class QueryConnectionCallback implements ConnectionCallback<T>, SqlProvider {

            @Override
            public T doInConnection(Connection con) throws SQLException, DataAccessException {
                con.setCatalog(catalog);
                Statement stmt = con.createStatement();
                ResultSet rs = null;
                try {
                    rs = stmt.executeQuery(sql);
                    return rse.extractData(rs);
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
            }

            @Override
            public String getSql() {
                return sql;
            }
        }
        return execute(new QueryConnectionCallback());
    }

    private int[] batchUpdate(String catalog, final String... sql) throws DataAccessException {
        class BatchUpdateConnectionCallback implements ConnectionCallback<int[]>, SqlProvider {
            private String currSql;

            @Override
            public int[] doInConnection(Connection con) throws SQLException, DataAccessException {
                con.setCatalog(catalog);
                Statement stmt = con.createStatement();
                int[] rowsAffected = new int[sql.length];
                if (JdbcUtils.supportsBatchUpdates(con)) {
                    for (String sqlStmt : sql) {
                        String pureSql = sqlStmt.replaceAll(";$", "");
                        this.currSql = appendSql(this.currSql, pureSql);
                        stmt.addBatch(pureSql);
                    }
                    try {
                        rowsAffected = stmt.executeBatch();
                    } catch (BatchUpdateException ex) {
                        String batchExceptionSql = null;
                        for (int i = 0; i < ex.getUpdateCounts().length; i++) {
                            if (ex.getUpdateCounts()[i] == Statement.EXECUTE_FAILED) {
                                batchExceptionSql = appendSql(batchExceptionSql, sql[i]);
                            }
                        }
                        if (StringUtils.hasLength(batchExceptionSql)) {
                            this.currSql = batchExceptionSql;
                        }

                        throw ex;
                    }
                } else {
                    for (int i = 0; i < sql.length; i++) {
                        this.currSql = sql[i];
                        if (!stmt.execute(sql[i])) {
                            rowsAffected[i] = stmt.getUpdateCount();
                        } else {
                            throw new InvalidDataAccessApiUsageException("Invalid batch SQL statement: " + sql[i]);
                        }
                    }
                }
                return rowsAffected;
            }

            private String appendSql(@Nullable String sql, String statement) {
                return (StringUtils.isEmpty(sql) ? statement : sql + "; " + statement);
            }

            @Override
            public String getSql() {
                return this.currSql;
            }
        }
        int[] result = execute(new BatchUpdateConnectionCallback());
        Assert.state(result != null, "No update counts");
        return result;
    }

    @Override
    public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object must not be null");

        Connection con = DataSourceUtils.getConnection(obtainDataSource());
        try {
            Connection conToUse = createConnectionProxy(con);
            return action.doInConnection(conToUse);
        } catch (SQLException ex) {
            String sql = getSql(action);
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            SQLException sqlException = ex;
            while (sqlException.getCause() instanceof SQLException) {
                sqlException = (SQLException) sqlException.getCause();
            }
            throw new ExecuteException(sql, sqlException);
        } finally {
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        } else {
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        if (destroy != null) {
            destroy.accept(super.getDataSource());
        }
    }
}
```