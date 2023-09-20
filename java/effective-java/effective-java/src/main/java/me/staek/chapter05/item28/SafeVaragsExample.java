package me.staek.chapter05.item28;

import java.util.List;

public class SafeVaragsExample {

    /**
     * SafeVarargs 애노테이션은 이런 데이터를 변조할 수 있는 로직에 사용하는 게 아님
     */
//    @SafeVarargs
    static void notSafe(List<String>... stringLists) {
        Object[] array = stringLists; // 런타임에 List<String>... 은 List[]이 된다.
                                      // 배열은 공변이기 때문에 컴파일을 통과한다. (List[] to Object[])
        List<Integer> tmpList = List.of(42);
        array[0] = tmpList;     // Object[0] 에 List 타입이 할당.
                                // Semantically invalid, but compiles without warnings
        String s = stringLists[0].get(0); // ClassCastException at runtime.
    }

    /**
     * SafeVarargs 애노테이션은 아래처럼 조회만 하는 용도 정도에서만 사용하는게 좋다.
     */
    @SafeVarargs
    static <T> void safe(T... values) {
        for (T value: values) {
            System.out.println(value);
        }
    }

    public static void main(String[] args) {
        SafeVaragsExample.safe("a", "b", "c");
        SafeVaragsExample.notSafe(List.of("a", "b", "c"));
    }

}
