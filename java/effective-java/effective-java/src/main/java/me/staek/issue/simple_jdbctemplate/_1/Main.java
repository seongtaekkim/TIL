package me.staek.issue.simple_jdbctemplate._1;

import java.io.File;
import java.sql.*;

/**
 * *** context ***
 * connection
 * preparestatement
 * execute
 * resultset
 * exception handle
 * resource release
 *
 * TODO 컨텍스틍에 해당하는 로직을 절차지향코드로 작성해보자.
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {


        File file = new File(".");

        String url = "jdbc:sqlite:" + file.getAbsolutePath() + "/data/dev.db";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url);
            String sql = "create table if not exists users (id integer primary key, name text not null, password text not null);";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            sql = "insert into users (id, name, password) values (?,?,?);";
            ps = conn.prepareStatement(sql);
            User user = new User();
            user.setId("1");
            user.setName("kim");
            user.setPassword("1111");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();

            sql = "select id, name, password from users;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
            }

            sql = "delete from users";
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
