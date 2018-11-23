```java
public interface DataSourceManager {

    DataSource build(DatabaseResourceProperties properties);
    DataSource flush(DataSource dataSource, DatabaseResourceProperties properties);
    void close(DataSource dataSource);
    
    
    // 默认实现
    class DefaultDataSourceManager implements DataSourceManager {
        @Override
        public DataSource build(DatabaseResourceProperties properties) {
            SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
            flush(dataSource, properties);
            dataSource.setConnectionProperties(properties.getProperties());
            return dataSource;
        }
        @Override
        public DataSource flush(DataSource dataSource, DatabaseResourceProperties properties) {
            if (dataSource == null || properties == null) {
                return dataSource;
            }
            SingleConnectionDataSource source = (SingleConnectionDataSource) dataSource;
            DatabaseAuth auth = properties.getAuth();
            String driverClassName = properties.getDriverClassName();
            source.setUrl(DataSourceManager.buildMysqlUrl(properties.getHost(), properties.getPort(), properties.getDbName()));
            source.setUsername(auth.getUsername());
            source.setPassword(auth.getPassword());
            source.setConnectionProperties(properties.getProperties());
            if (driverClassName != null) {
                source.setDriverClassName(driverClassName);
            }
            return source;
        }
        @Override
        public void close(DataSource dataSource) {
            SingleConnectionDataSource source = (SingleConnectionDataSource) dataSource;
            source.destroy();
        }
    }
}
```


```java
public class HikariDataSourceManager implements DataSourceManager {
    @Override
    public DataSource build(DatabaseResourceProperties properties) {
        if (properties == null) {
            return null;
        }
        return flush(new HikariDataSource(), properties);
    }
    @Override
    public DataSource flush(DataSource dataSource, DatabaseResourceProperties properties) {
        if (dataSource == null || properties == null) {
            return dataSource;
        }
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        String jdbcUrl = hikariDataSource.getJdbcUrl();
        if (jdbcUrl == null) {
            jdbcUrl = "";
        }
        URI uri = URI.create(jdbcUrl.replaceAll("^(j|J)(d|D)(b|B)(c|C):", ""));
        String host = properties.getHost();
        Short port = properties.getPort();
        if (!Objects.equals(host, uri.getHost()) || !Objects.equals(port, uri.getPort()) || !Objects.deepEquals(properties.getProperties(), hikariDataSource.getDataSourceProperties())) {
            hikariDataSource.close();
            hikariDataSource = new HikariDataSource();
            hikariDataSource.setPoolName(host.concat(":").concat(String.valueOf(port)));
            hikariDataSource.setJdbcUrl(DataSourceManager.buildMysqlUrl(host, port, properties.getDbName()));
            hikariDataSource.setDataSourceProperties(properties.getProperties());
        }
        DatabaseAuth auth = properties.getAuth();
        hikariDataSource.setUsername(auth.getUsername());
        hikariDataSource.setPassword(auth.getPassword());
        String driverClassName = properties.getDriverClassName();
        if (driverClassName != null) {
            hikariDataSource.setDriverClassName(driverClassName);
        }
        return hikariDataSource;
    }
    @Override
    public void close(DataSource dataSource) {
        if (dataSource == null) {
            return;
        }
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            hikariDataSource.close();
        }
    }
}

```










```java 
public interface DataSourceManager {
    /**
     * 新建 DataSource
     */
    DataSource create(DatabaseResource resource);

    /**
     * 更新 DataSource
     */
    DataSource update(DataSource dataSource, DatabaseResource resource);

    /**
     * DataSource 属性是否相等, 例如 用户名 密码 url
     */
    Boolean isEquals(DataSource d1, DataSource d2);

    /**
     * 删除 DataSource
     */
    void destroy(DataSource dataSource);
}
```
