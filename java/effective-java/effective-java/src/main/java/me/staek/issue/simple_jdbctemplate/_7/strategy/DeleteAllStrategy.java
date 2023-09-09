package me.staek.issue.simple_jdbctemplate._7.strategy;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStrategy implements PreparedStretegy {

    @Override
    public PreparedStatement createStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("delete from users");
    }
}
