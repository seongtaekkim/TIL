package me.staek.issue.simple_jdbctemplate._5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private Datasource datasource;

    public UserDao(Datasource datasource) {
        this.datasource = datasource;
    }

    public void createTable() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();
            String sql = "create table if not exists users (id integer primary key, name text not null, password text not null);";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (ps != null) { try { ps.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
        }
    }

    public void add() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();
            String sql = "insert into users (id, name, password) values (?,?,?);";
            ps = conn.prepareStatement(sql);
            User user = new User();
            user.setId("1");
            user.setName("kim");
            user.setPassword("1111");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (ps != null) { try { ps.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
            if (conn != null) { try { conn.close(); } catch (SQLException e) { throw new RuntimeException(e); } }
        }
    }

    public void get() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();

            String sql = "select id, name, password from users;";
            ps = conn.prepareStatement(sql);
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

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = datasource.newConnection();

            String sql = "delete from users";
            ps = conn.prepareStatement(sql);
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
