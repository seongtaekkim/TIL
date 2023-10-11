package me.staek.chapter05.item32.heappollution;

import java.util.List;

/**
 * 힙오염 예제1
 * 제네릭과 varargs를 혼용하면 타입 안전성이 깨진다!
 * 제네릭 varargs 배열 매개변수에 값을 저장하는 것은 안전하지 않다.
 */
public class Dangerous {
    static void dangerous(List<String>... stringLists) {
        List<Integer> intList = List.of(42);
        Object[] objects = stringLists; // 제네릭은 소거되기에, Object[] <== List[] 는 공변이므로 가능.
        /**
         * Object 에 List 는 저장이 가능하다.
         * 하지만 실제 타입은 String인 배열에 Integer타입을 입력하므로
         * 힙 오염이 발생.
         */
        objects[0] = intList;
        /**
         * List가변배열은 값을 꺼낼 때 Object타입에서 String으로 cating하며 에러 발생. (실제는 Integer)
         * ClassCastException
         */
        String s = stringLists[0].get(0);
    }

    public static void main(String[] args) {
        dangerous(List.of("effective")
                ,List.of("java"));

    }
}
