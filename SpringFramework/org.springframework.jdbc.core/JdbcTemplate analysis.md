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



#### PreparedStatementCallback<T>

~~~java
T doInpreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException;
~~~





#### JdbcTemplate extends JdbcAccessor implements JdbcOperations

~~~java
public JdbcTemplate() {}
public JdbcTemplate(DataSource dataSource, boolean lazyInit) {
    setDataSource(dataSource);
    //setLazyInit(lazyInit);
    //afterPropertiesSet();
}
public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		try {
			Connection conToUse = con;
			if (this.nativeJdbcExtractor != null) {
		    // Extract native JDBC Connection, castable to OracleConnection or the like.
				conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
			}
			else {
		// Create close-suppressing Connection proxy, also preparing returned Statements.
				conToUse = createConnectionProxy(con);
			}
			return action.doInConnection(conToUse);
		}
		catch (SQLException ex) { ... } finally { ... } 
}


public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
		return query(psc, null, rse);
}


	/**
	 * Query using a prepared statement, allowing for a PreparedStatementCreator
	 * and a PreparedStatementSetter. Most other query methods use this method,
	 * but application code will always work with either a creator or a setter.
	 * @param psc Callback handler that can create a PreparedStatement given a
	 * Connection
	 * @param pss object that knows how to set values on the prepared statement.
	 * If this is null, the SQL will be assumed to contain no bind parameters.
	 * @param rse object that will extract results.
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 * @throws DataAccessException if there is any problem
	 */
// PreparedStatementCreator : PreparedStatement 객체생성 콜백객체
// PreparedStatementSetter  : null일 경우 bind parameter가 없는 케이스.
// ResultSetExtractor<T>    : 추출한 결과를 리턴받은 오브젝트
/*
*  전체건수를 조회하기 위해 해당 쿼리와 결과 integer를 결과로 리턴받을 수 있는 기능.
*/
public <T> T query( PreparedStatementCreator psc, final PreparedStatementSetter pss, 						final ResultSetExtractor<T> rse)
			throws DataAccessException {

		return execute(psc, new PreparedStatementCallback<T>() {
			@Override
			public T doInPreparedStatement(PreparedStatement ps) throws SQLException {
				ResultSet rs = null;
				try {
					if (pss != null) {
						pss.setValues(ps);
					}
					rs = ps.executeQuery();
					ResultSet rsToUse = rs;
					if (nativeJdbcExtractor != null) {
						rsToUse = nativeJdbcExtractor.getNativeResultSet(rs);
					}
					return rse.extractData(rsToUse);
				}
				finally {
					JdbcUtils.closeResultSet(rs);
					if (pss instanceof ParameterDisposer) {
						((ParameterDisposer) pss).cleanupParameters();
					}
				}
			}
		});
	}
~~~









