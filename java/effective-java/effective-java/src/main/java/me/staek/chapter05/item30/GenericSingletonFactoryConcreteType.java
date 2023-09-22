package me.staek.chapter05.item30;

import java.util.function.Function;


/**
 * 항등함수 구현 (Function.identity())
 *
 * - 단점: 타입별로 구현해야 함
 *        매번 인스턴스가 생성됨.
 */
public class GenericSingletonFactoryConcreteType {

    public static Function<String, String> stringIdentityFunction() {
        return new Function<String, String>() {
            @Override
            public String apply(String t) {
                return t;
            }
        };
    }

    public static Function<Number, Number> integerIdentityFunction() {
        return new Function<Number, Number>() {
            @Override
            public Number apply(Number t) {
                return t;
            }
        };
    }

    public static void main(String[] args) {
        String[] strings = { "삼베", "대마", "나일론" };
        Function<String, String> sameString = stringIdentityFunction();
        for (String s : strings)
            System.out.println(sameString.apply(s));

        Number[] numbers = { 1, 2.0, 3L };
        Function<Number, Number> sameNumber = integerIdentityFunction();
        for (Number n : numbers)
            System.out.println(sameNumber.apply(n));
    }
}
