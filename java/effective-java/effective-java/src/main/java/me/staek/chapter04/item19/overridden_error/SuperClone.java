package me.staek.chapter04.item19.overridden_error;

public class SuperClone implements Cloneable {

    private String name;

    protected void test() {
        System.out.println("call item");
    }

    @Override
    public SuperClone clone() {
        Object clone = null;
        try {
            /**
             * 자식클래스의 clone()이후 상태를 변경하기 전에
             * 자식클래스의 test() 가 호출된다.
             */
            test();
            clone = super.clone();
            return (SuperClone) clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
