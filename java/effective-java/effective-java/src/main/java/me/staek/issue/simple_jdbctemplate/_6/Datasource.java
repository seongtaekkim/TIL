package me.staek.issue.simple_jdbctemplate._6;

import java.sql.Connection;
import java.sql.SQLException;

public interface Datasource {
    Connection newConnection() throws SQLException;

}
