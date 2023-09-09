package me.staek.issue.simple_jdbctemplate._9;


import me.staek.issue.simple_jdbctemplate._9.strategy.GetAllStrategy;
import me.staek.issue.simple_jdbctemplate._9.strategy.PreparedStretegy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private Datasource datasource;
    private JDBCContext context;

    public UserDao(Datasource datasource) {
        this.datasource = datasource;
        context = new JDBCContext(datasource);
    }


    public void createTable() {
        context.executeSql("create table if not exists users (id integer primary key, name text not null, password text not null);");
    }

    public void add() {

        User user = new User();
        user.setId("1");
        user.setName("kim");
        user.setPassword("1111");

        this.context.context(new PreparedStretegy() {

            @Override
            public PreparedStatement createStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("insert into users (id, name, password) values (?,?,?);");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    public void get() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();
            PreparedStretegy strategy = new GetAllStrategy();
//            PreparedStretegy strategy = new GetAllStrategy();
            ps = strategy.createStatement(conn);
            rs = ps.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (ps != null) { try { ps.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
        }
    }

    public void deleteAll() {
        context.executeSql("delete from users");
    }

}
