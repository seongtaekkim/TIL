package me.staek.issue.simple_jdbctemplate._6;

/**
 * preparedstatement 메서드추출
 * -> 재사용하지 않으므로 실익이 없다.
 * -> 오히려 나머지 context 부분이 재사용 된다.
 *
 *
 *  템플릿메서드패턴 적용
 *  - UserDao를 다시 추상화 클래스로 만들고
 *  - 이를 상속받은 클래스가 추출한 preparedstatement 메서드를 구현하도록 한다.
 *
 *  문제점
 *  -> 상속을 해야 한다.
 *  -> 요청마다 클래스가 늘어나야 한다.
 *  -> 팩토리도 요청마다 함수가 늘어나야 한다.
 *
 */
public class Main {
    public static void main(String[] args) {

//        UserDao userDao = new DaoFactory().userDao();

        UserDao userDao = new DaoFactory().createTableUserDao();
        userDao.createTable();

        userDao = new DaoFactory().addUserDao();
        userDao.add();

        userDao = new DaoFactory().getUserDao();
        userDao.get();

        userDao = new DaoFactory().deleteAllUserDao();
        userDao.deleteAll();
    }
}
