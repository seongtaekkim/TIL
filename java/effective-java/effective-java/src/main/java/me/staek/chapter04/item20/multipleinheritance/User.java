package me.staek.chapter04.item20.multipleinheritance;

/**
 * 추상클래스를 상속한 상황에서 다른 추상클래스를 상속하고 싶을 때 사용하는 방법이다.
 * - 새로운 추상클래스를 inner class 에서 상속받아 구현한 뒤
 * - 맴버객체로 정의해서 사용할 수 있다.
 */
public class User extends AbstractUser implements Role {

    private final MyRole role = new MyRole();
    @Override
    void userName() {
        System.out.println("my name is seongtaek");
    }

    @Override
    public void roleName() {
        this.role.roleName();
    }

    private static class MyRole extends AbstractRole {

        @Override
        public void roleName() {
            System.out.println("rolename is super");
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.userName();
        user.roleName();
    }

}
