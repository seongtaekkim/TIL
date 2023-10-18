package me.staek.chapter06.item40;

public interface _03_Test2 extends _03_Test {

    @Override
    default void test() {
        System.out.println("default override test");
    }

    default void test2() {
        System.out.println("default test ");
    }
}
