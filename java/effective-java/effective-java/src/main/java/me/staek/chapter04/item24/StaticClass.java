package me.staek.chapter04.item24;

/**
 * 정적 맴버 클래스는 outer class 에 독립적이므로
 * outer class의 인스턴스 생성과 관련없이 사용할 수 있다.
 * 한편 인스턴스의 리소스는 사용할 수 없다.
 */
public class StaticClass {

    private static int number = 10;

    static private class InnerClass {
        void doSomething() {
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        InnerClass innerClass = new InnerClass();
        innerClass.doSomething();

    }
}
