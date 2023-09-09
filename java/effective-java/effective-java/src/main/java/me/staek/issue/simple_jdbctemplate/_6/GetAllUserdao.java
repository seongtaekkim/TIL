package me.staek.issue.simple_jdbctemplate._6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetAllUserdao extends UserDao {
    public GetAllUserdao(Datasource datasource) {
        super(datasource);
    }
    protected PreparedStatement createStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("select id, name, password from users;");
    }
}
