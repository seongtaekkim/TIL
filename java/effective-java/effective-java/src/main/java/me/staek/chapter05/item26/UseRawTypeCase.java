package me.staek.chapter05.item26;

/**
 * rawtype을 사용하는 경우 2가지
 *
 * 1. "매개변수화타입".class : 컴파일 이후 어차피 정규타입 매개변수는 소거되기 때문에 의미가 없다.
 *    -> 사용하려 하면 컴파일에러 발생.
 * 2. "검사할 타입" instanceof "매개변수화타입" : 컴파일 이후 소거되어 의미 없음.
 *    -> 비한정적 와일드카드는 사용이 가능한데, rawtype과 똑같이 기능함
 */
public class UseRawTypeCase<E> {

    private E e;

    public static void check(Object o) {
        if (o instanceof UseRawTypeCase)
//        if (o instanceof UseRawType<?>)
            System.out.println(true);
        else
            System.out.println(false);
    }

    public static void main(String[] args) {
        System.out.println(UseRawTypeCase.class);

        UseRawTypeCase<String> stringType = new UseRawTypeCase<>();
        UseRawTypeCase<Integer> intType = new UseRawTypeCase<>();
        check(stringType);
        check(intType);
    }
}
