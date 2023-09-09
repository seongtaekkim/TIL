package me.staek.issue.simple_jdbctemplate._6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserdao extends UserDao {
    public AddUserdao(Datasource datasource) {
        super(datasource);
    }
    protected PreparedStatement createStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("insert into users (id, name, password) values (?,?,?);");
    }
}
