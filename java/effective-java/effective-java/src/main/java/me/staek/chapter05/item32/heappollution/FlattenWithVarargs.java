package me.staek.chapter05.item32.heappollution;

import java.util.ArrayList;
import java.util.List;


/**
 *  제네릭과 varargs를 혼용할 때 안전하게 사용하는 방법 1
 */
public final class FlattenWithVarargs {

    /**
     * lists 에 무언가를 넣는 로직을 작성하지 않는다.
     * lists에서 값을 꺼내서 다른 리소스에 입력하거나 하면 안전하다.
     */
    @SafeVarargs
    static <T> List<T> flatten(List<? extends T>... lists) {
        /**
         * 가변배열에서 값을 하나씩 꺼내서 result에 입력한다.
         * (값을 입력하기에 producer에 해당함.)
         */
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists)
            result.addAll(list);
        return result;
    }

    public static void main(String[] args) {
        List<Integer> flatList = flatten(
                List.of(1, 2), List.of(3, 4, 5), List.of(6,7));
        System.out.println(flatList);
    }
}
