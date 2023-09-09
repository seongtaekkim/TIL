package me.staek.issue.simple_jdbctemplate._11;

import java.util.Arrays;
import java.util.List;

/**
 * get 함수 리펙토링
 * - ResultSet 을 객체(User) 하나로 매핑해주는 Rowmapper<T> interface 제작
 * - ResultSet을 전략으로 추출하여 결과를 list 형태로 생성해주는 ResultSetStrategy<T> interface, RowByResultSet<T> class 생성
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
        List<User> users = userDao.get();
        Arrays.stream(users.toArray()).forEach(System.out::println);
        userDao.deleteAll();
    }
}
