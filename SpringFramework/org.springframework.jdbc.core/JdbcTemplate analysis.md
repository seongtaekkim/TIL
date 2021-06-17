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











JdbcTemplate > update(sql)

~~~java
this.jdbcTemplate.update("delete from users");
~~~

(1)  함수 호출





JdbcTemplate > update(final String sql)

~~~java
public int update(final String sql) throws DataAccessException {
    Assert.notNull(sql, "SQL must not be null");
    if (logger.isDebugEnabled()) {
        logger.debug("Executing SQL update [" + sql + "]");
    }
    class UpdateStatementCallback implements StatementCallback<Integer>, SqlProvider {
        @Override
        public Integer doInStatement(Statement stmt) throws SQLException {
            int rows = stmt.executeUpdate(sql);
            if (logger.isDebugEnabled()) {
                logger.debug("SQL update affected " + rows + " rows");
            }
            return rows;
        }
        @Override
        public String getSql() {
            return sql;
        }
    }
    return execute(new UpdateStatementCallback());
}
~~~

(1)  StatementCallback 인터페이스로 전략을 만들었는데,  익명클래스가 아닌 내부클래스로 되어있고, 함수를 두개 구현하였다.

(2) doInStatement는 Statement를 받아 실제 쿼리를실행한다.

(3) sql을 리턴한다.



#### StatementCallback<T>

~~~java
T doInStatement(Statement stmt) throws SQLException, DataAccessException;
~~~

#### SqlProvider

~~~java
String getSql();
~~~





#### JdbcTemplate > <T> T execute(StatementCallback<T>)

~~~java
	@Override
	public <T> T execute(StatementCallback<T> action) throws DataAccessException {
		Assert.notNull(action, "Callback object must not be null");

		Connection con = DataSourceUtils.getConnection(getDataSource());
		Statement stmt = null;
		try {
			Connection conToUse = con;
			if (this.nativeJdbcExtractor != null &&
					this.nativeJdbcExtractor.isNativeConnectionNecessaryForNativeStatements()) {
				conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
			}
			stmt = conToUse.createStatement();
			applyStatementSettings(stmt);
			Statement stmtToUse = stmt;
			if (this.nativeJdbcExtractor != null) {
				stmtToUse = this.nativeJdbcExtractor.getNativeStatement(stmt);
			}
			T result = action.doInStatement(stmtToUse);
			handleWarnings(stmt);
			return result;
		}
		catch (SQLException ex) {
			// Release Connection early, to avoid potential connection pool deadlock
			// in the case when the exception translator hasn't been initialized yet.
			JdbcUtils.closeStatement(stmt);
			stmt = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", getSql(action), ex);
		}
		finally {
			JdbcUtils.closeStatement(stmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
~~~

(1) connection 객체를 얻는다.

(2) doInStatement 함수를 실행한다.

(3) T를 리턴받아 리턴한다. (여기서는 int)













