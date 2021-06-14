org.springframework.jdbc.core > jdbcTemplate 분석



#### JdbcOperation

~~~java
<T> T execute(ConnectionCallback<T> action) throws DataAccessException

<T> T execute(StatementCAllback<T> action) throws DataAccessException;

void execute(String sql) throws DataAccessException;

<T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException;

void query(String sql, RowCallbackHandler rch) throws DataAccessException;

<T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;

<T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException;
......
~~~



#### ConnectionCallback<T>

~~~java
T doInconnection(Connection con) throws SQLException, DataAccessException;
~~~



#### ResultSetExtractor<T>

~~~java
T  extractData(ResultSet rs) throws SQLException, DataAccessException;
~~~



#### RowCallbackHandler

~~~java
void processRow(ResultSet rs) throws SQLException;
~~~



#### RowMapper<T>

~~~java
T mapRow(ResultSet rs, int rowNum) throws SQLException;
~~~





#### JdbcTemplate

~~~java
public JdbcTemplate() {}
public JdbcTemplate(DataSource dataSource, boolean lazyInit) {
    setDataSource(dataSource);
    //setLazyInit(lazyInit);
    //afterPropertiesSet();
}
~~~









