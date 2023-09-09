package me.staek.issue.simple_jdbctemplate._11;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private JDBCContext context;

    public UserDao(Datasource datasource) {
        context = new JDBCContext(datasource);
    }

    private static Rowmapper<User> rowmapper = new Rowmapper<>() {
        @Override
        public User row(ResultSet rs) throws SQLException {
            User user = new User();
            user.setId(rs.getString(1));
            user.setName(rs.getString(2));
            user.setPassword(rs.getString(3));
            return user;
        }
    };

    public void createTable() {
        context.executeSql("create table if not exists users (id integer primary key, name text not null, password text not null);");
    }

    public void add(User user) {
        context.executeSql("insert into users (id, name, password) values (?,?,?);", user.getId(), user.getName(), user.getPassword() );
    }

    public List<User> get() {
        List<User> users = context.executeSql("select id, name, password from users;", rowmapper);
        return users;


    }

    public void deleteAll() {
        context.executeSql("delete from users");
    }

}
