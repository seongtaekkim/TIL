package me.staek.issue.simple_jdbctemplate._11;


import me.staek.issue.simple_jdbctemplate._11.strategy.PreparedStretegy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    /**
     * createtable, deleteAll
     */
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
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * add
     */
    public void context(PreparedStretegy strategy, Object[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();
            ps = strategy.createStatement(conn);

            for (int i = 0; i < args.length; i++) {
                ps.setString(i + 1, args[i].toString());
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * get all
     */
    public <T> T context(PreparedStretegy pss, ReseultSetStrategy<T> rss) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();
            PreparedStatement ps = pss.createStatement(conn);
            rs = ps.executeQuery();
            return rss.getDate(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
        }

    }

    public void executeSql(String sql, Object... args) {
        this.context(new PreparedStretegy() {
            @Override
            public PreparedStatement createStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("insert into users (id, name, password) values (?,?,?);");
                return ps;
            }
        }, args);
    }


    public <T> List<T> executeSql(String sql, Rowmapper<T> rowmapper) {
        List<T> context = context(new PreparedStretegy() {
            @Override
            public PreparedStatement createStatement(Connection conn) throws SQLException {
                return conn.prepareStatement(sql);
            }
        }, new RowByResultSet<T>(rowmapper));
        return context;
    }

}