package me.staek.issue.simple_jdbctemplate._5;

/**
 * 팩토리 생성
 * - 클라이언트에서의 datasource 생성책임을 factory 에서 하도록 변경하였다.
 *   -> 팩토리가 확장하면 스프링의 DI가 될 것이다.
 */
public class Main {
    public static void main(String[] args) {

        UserDao userDao = new DaoFactory().userDao();
        userDao.createTable();
        userDao.add();
        userDao.get();
        userDao.deleteAll();
    }
}
