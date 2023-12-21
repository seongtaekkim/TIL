package me.staek.chapter10.item73;


class Inner {
    public void test() {
        try {
            int a = 1/0;
        } catch (ArithmeticException e) {
            throw new RuntimeException(e);
        }
    }
}
class Outer {

    public void test() {
        try {
            new Inner().test();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
public class Test {
    public static void main(String[] args) {
        Outer outer = new Outer();
        outer.test();
    }
}
