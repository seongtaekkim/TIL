package me.staek.chapter04.item24;

/**
 * 비정적 멤버 클래스는 outer class 인스턴스가 생성되어야 사용이 가능하고
 * 해당 리소스들을 사용할 수 있다.
 *
 * inner class 가능한 문법:  OutterClass.this.메서드
 * 외부에서 innerclass instanace 생성가능 문법 : new OutterClass().new InnerClass();
 */
public class OutterClass {

    private int number = 10;

    void printNumber() {
        InnerClass innerClass = new InnerClass();
        System.out.println(111);
    }

    private class InnerClass {
        void doSomething() {
            System.out.println(number);
            OutterClass.this.printNumber();
        }
    }

    public static void main(String[] args) {
        InnerClass innerClass = new OutterClass().new InnerClass();
        innerClass.doSomething();
    }

}
