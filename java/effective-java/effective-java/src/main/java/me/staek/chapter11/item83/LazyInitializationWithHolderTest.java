package me.staek.chapter11.item83;

/**
 * static class내부 자원은 어느 시점에 생성될까?
 * - 클래스로더 상황에서 field는 실행되지 않음.
 * - top-level 클래스 인스턴스 생성한다고 해서 실행하지 않음.
 * - FieldHolder class가 호출되는 시점에 static 데이터가 메모리에 로딩된다.
 */
public class LazyInitializationWithHolderTest {
    private static class FieldHolder {
        static int data = 10;
        static final FieldType field = new FieldType(5);
    }

    private static FieldType getField() {
        return FieldHolder.field;
    }

    public static void main(String[] args) {
//        new LazyInitializationWithHolderTest();

        // FieldHolder class 접근
//        new FieldHolder();
//        System.out.println(FieldHolder.data);

        // FieldHolder 의 static method 접근
        LazyInitializationWithHolderTest.getField();
    }
}
