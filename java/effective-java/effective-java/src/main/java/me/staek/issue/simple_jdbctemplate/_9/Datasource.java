package me.staek.issue.simple_jdbctemplate._9;

import java.sql.Connection;
import java.sql.SQLException;

public interface Datasource {
    Connection newConnection() throws SQLException;

}
