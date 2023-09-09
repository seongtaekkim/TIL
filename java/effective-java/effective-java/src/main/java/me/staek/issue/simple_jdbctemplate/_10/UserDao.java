package me.staek.issue.simple_jdbctemplate._10;



import me.staek.issue.simple_jdbctemplate._10.strategy.GetAllStrategy;
import me.staek.issue.simple_jdbctemplate._10.strategy.PreparedStretegy;

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

    public void add(User user) {
        context.executeSql("insert into users (id, name, password) values (?,?,?);", user.getId(), user.getName(), user.getPassword() );
    }

    public void get() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();
            PreparedStretegy strategy = new GetAllStrategy();
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
