package me.staek.issue.simple_jdbctemplate._11;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowByResultSet<T> implements ReseultSetStrategy<List<T>> {

    private final Rowmapper<T> rowmapper;
    public RowByResultSet(Rowmapper<T> rowmapper) {
        this.rowmapper = rowmapper;
    }
    @Override
    public List<T> getDate(ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rowmapper.row(rs));
        }
        return list;
    }
}
