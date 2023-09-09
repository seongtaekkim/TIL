package me.staek.issue.simple_jdbctemplate._9;


import me.staek.issue.simple_jdbctemplate._9.strategy.PreparedStretegy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCContext {

    private Datasource datasource;

    public JDBCContext(Datasource datasource) {
        this.datasource = datasource;
    }

    public void executeSql(String sql) {
        context(new PreparedStretegy() {
            @Override
            public PreparedStatement createStatement(Connection conn) throws SQLException {
                return conn.prepareStatement(sql);
            }
        });
    }

    public void context(PreparedStretegy strategy) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();
            ps = strategy.createStatement(conn);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (ps != null) { try { ps.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
        }
    }
}
