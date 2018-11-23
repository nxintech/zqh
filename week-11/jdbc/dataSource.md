DataSourceManager 用来管理 dataSource

通过创建新的 dataSource , 删除老的 dataSource 用来支持动态添加/删除数据库


接口定义
```java
public interface DataSourceManager {

    DataSource build(DatabaseResourceProperties properties);
    DataSource flush(DataSource dataSource, DatabaseResourceProperties properties);
    void close(DataSource dataSource);
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

语义

build, 实际上调用 flush 

flush 逻辑: 

1 传入一个 dataSource, 和一个 properties

2 properties 和 dataSource properties 相同, 返回 dataSource

3 如果不同, 根据 properties 创建 dataSource, 并返回新 dataSource


build 耦合了  flush

既然是新创建的 dataSource, dataSource 为什么要和 properties 做对比? 



## after
新接口设计

根据需求 重新定义为下面的接口

```java 
public interface DataSourceManager {
    /**
     * 新建 DataSource
     */
    DataSource create(DatabaseResource resource);

    /**
     * 替换 DataSource
     */
    DataSource replace(DataSource oldDs, DataSource newDs);

    /**
     * 对比两个 DataSource 属性是否相等, 例如 用户名,密码,url 等
     */
    Boolean propEquals(@NonNull DataSource a, @NonNull DataSource b);

    /**
     * 删除 DataSource
     */
    void destroy(@NonNull DataSource dataSource);
}
```
实现


```java
public class HikariDataSourceManager implements DataSourceManager {

    @Override
    public DataSource create(@NonNull DatabaseResource resource) {
        HikariDataSource dataSource = new HikariDataSource();
        String url = DataSourceManager.mysqlJdbcUrl(resource.getHost(), resource.getPort(), resource.getCatalog());

        dataSource.setJdbcUrl(url);
        dataSource.setUsername(resource.getAuth().getUsername());
        dataSource.setPassword(resource.getAuth().getPassword());
        dataSource.setDataSourceProperties(resource.getDataSourceProperties());

        dataSource.setPoolName(url);
        dataSource.setMaximumPoolSize(2);
        dataSource.setConnectionTestQuery("SELECT 1");

        return dataSource;
    }

    @Override
    public DataSource replace(DataSource oldDs, DataSource newDs) {
        if (propEquals(oldDs, newDs)) {
            destroy(newDs);
            return oldDs;
        } else {
            destroy(oldDs);
            return newDs;
        }
    }

    @Override
    public Boolean propEquals(DataSource d1, DataSource d2) {
        HikariDataSource left = (HikariDataSource) d1;
        HikariDataSource right = (HikariDataSource) d2;
        return Objects.equals(left.getJdbcUrl(), right.getJdbcUrl())
                && Objects.equals(left.getUsername(), right.getUsername())
                && Objects.equals(left.getPassword(), right.getPassword())
                && Objects.equals(left.getDataSourceProperties(), right.getDataSourceProperties());
    }
    
    @Override
    public void destroy(DataSource dataSource) {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            hikariDataSource.close();
        }
    }
}
```

usage 对比
```java
DataSoruce dataSoruce = new HikariDataSourceManager().flush(old, resourcePrppites);

HikariDataSourceManager dsm = new HikariDataSourceManager();
DataSoruce dataSoruce = dsm.replace(old, dsm.create(resourcePrppites));
```