package me.staek.chapter06.item38;


enum Study {
    effective("one"),
    java("two");
    private final String value;
    Study(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

class Staek {

}


/**
 * 1) 선언된 순서대로 이 Class 객체가 나타내는 enum 클래스를 구성하는 값을 포함하는 배열을 반환
 * 2) Class 객체가 enum 유형을 나타내지 않는 경우 null을 반환.
 */
public class EnumConstantsTest {

    public static void main(String[] args) {

        // 정의된 enum type의 상수를 조회
        Class cls = Study.class;
        for(Object obj: cls.getEnumConstants()) {
            System.out.println(obj);
        }

        // enum이 아니면 null 리턴
        Class cls2 = Staek.class;
        System.out.println(cls2.getEnumConstants());


        // enum type 만 호출되게, enum 타입이 아니면 컴파일 에러나도록 구성
        printEnumValues(Study.class);
//        printEnumValues(Staek.class); // enum이 아니면 컴파일에러
    }


    private static <E extends Enum<E>> void printEnumValues(Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E op : enumConstants) {
            System.out.println(op);
        }
    }
}
