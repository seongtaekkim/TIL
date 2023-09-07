package me.staek.chapter04.item19.overridden_error;

/**
 * 재정의 가능 메서드를 호출하는 생성자
 * - 자식 클래스가 override 했다면, 자식클래스의 함수를 호출하게 됨.
 * - 자식 클래스 생성자보다 부모 생성자가 먼저 호출되므로, 자식 인스턴스 생성 전에 함수가 호출되어 오동작함.
 */
public class Super {
    public Super() {
        overrideMe();
    }

    public void overrideMe() {
    }
}
