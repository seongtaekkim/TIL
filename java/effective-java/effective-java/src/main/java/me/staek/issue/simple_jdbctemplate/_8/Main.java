package me.staek.issue.simple_jdbctemplate._8;

/**
 *
 * 컨텍스트 함수추출
 * - 전략 인스턴스 생성 후 컨텍스트 함수에 인자로 전달하는 로직 생성
 *   -> createTable, deleteAll, add 에 적용
 *   -> get은 리턴에 대한 처리로직이 없어서 아직은 유지
 * - 익명클래스를 만들어서 콜백 형태로 만들어 전략 구현체 파일을 없앤다.
 *
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
