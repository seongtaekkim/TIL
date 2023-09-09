package me.staek.issue.simple_jdbctemplate._6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllUserdao extends UserDao {
    public DeleteAllUserdao(Datasource datasource) {
        super(datasource);
    }
    protected PreparedStatement createStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("delete from users");
    }
}
