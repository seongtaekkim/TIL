package me.staek.chapter11.item83;

/**
 * 초기화 종류
 */
public class Initialization {

    /**
     * 인스턴스 생성시 초기화 (지연초기화 아님)
     */
    private final FieldType field1 = computeFieldValue();

    /**
     * 인스턴스 메서드 동기화 지연 초기화
     * - 성능이슈
     */
    private FieldType field2;
    private synchronized FieldType getField2() {
        if (field2 == null)
            field2 = computeFieldValue();
        return field2;
    }

    /**
     * static field 지연초기화를 위한 Holder class
     * - getField() 메서드 호출 시
     */
    // Lazy initialization holder class idiom for static fields - Page 334
    private static class FieldHolder {
        static final FieldType field = computeFieldValue();
    }

    private static FieldType getField() { return FieldHolder.field; }




    /**
     * double checked locking
     */
    private volatile FieldType field4;
    private FieldType getField4() {
        FieldType result = field4;
        if (result != null)    // First check (no locking)
            return result;

        synchronized(this) {
            if (field4 == null) // Second check (with locking)
                field4 = computeFieldValue();
            return field4;
        }
    }


    /**
     * Single-check idiom
     *
     * 지역변수를 사용할 시 이점에 대해 설명하시오
     */
    private volatile FieldType field5;

    private FieldType getField5() {
        FieldType result = field5;
        if (result == null)
            field5 = result = computeFieldValue();
        return result;
    }

    private static FieldType computeFieldValue() {
        return new FieldType();
    }
}
