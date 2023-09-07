package me.staek.chapter04.item19.overridden_error;


/**
 * 자식클래스에서 clone 을 할때, 부모클래스에서 override 가능한 method를 호출하면,
 * 자식클래스의 super.clone() 이후 상태변경 등을 하기 전에 자식 mtehod가 호출된다.
 */
public class SubClone extends SuperClone implements Cloneable {

    private String name;

    @Override
    public SubClone clone() {
        System.out.println("subitem clone before");
        SubClone clone = (SubClone) super.clone();
        // clone instance 상태 변경 작업 ...
        System.out.println("subitem clone after");
        return clone;
    }

    @Override
    protected void test() {
        System.out.println("call subitem test ");
    }

    public static void main(String[] args) {
        SubClone item = new SubClone();
        SubClone clone = item.clone();
    }
}
