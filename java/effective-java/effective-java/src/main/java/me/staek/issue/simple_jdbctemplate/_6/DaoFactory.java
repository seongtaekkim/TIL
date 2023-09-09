package me.staek.issue.simple_jdbctemplate._6;

public class DaoFactory {

//    public UserDao userDao() {
//        JDBC jdbc = new JDBC();
//        return new UserDao(jdbc);
//    }

    public UserDao createTableUserDao() {
        JDBC jdbc = new JDBC();
        return new createTableUserdao(jdbc);
    }

    public UserDao addUserDao() {
        JDBC jdbc = new JDBC();
        return new AddUserdao(jdbc);
    }
    public UserDao getUserDao() {
        JDBC jdbc = new JDBC();
        return new GetAllUserdao(jdbc);
    }

    public UserDao deleteAllUserDao() {
        JDBC jdbc = new JDBC();
        return new DeleteAllUserdao(jdbc);
    }

}
