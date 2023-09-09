package me.staek.issue.simple_jdbctemplate._10;

/**
 * add 함수 refactoring
 * - 전달할 인자(User)를 가변인수로 전달받을 executeSql 메서드를 만들고
 * - context 메서드에서 statement 객체가 배열 값을 세팅하도록 변경한다.
 *   -> 우선은 setString 으로 일관되게 만들지만, 훗날 이를 다시 전략패턴으로 빼내어 타입체크 후 입력해야 한다.
 *
 */
public class Main {
    public static void main(String[] args) {

        UserDao userDao = new DaoFactory().userDao();

        userDao.createTable();
        User user = new User();
        user.setId("1");
        user.setName("kim");
        user.setPassword("1111");
        userDao.add(user);
        userDao.get();
        userDao.deleteAll();
    }
}
