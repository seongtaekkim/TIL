package me.staek.chapter05.item32.heappollution;

import java.util.ArrayList;
import java.util.List;

/**
 *  제네릭과 varargs를 혼용할 때 안전하게 사용하는 방법 2
 */
public class FlattenWithList {

    /**
     * 애초에 가변인자로 받지 않으면 안전하다.
     */
    static <T> List<T> flatten(List<List<? extends T>> lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists)
            result.addAll(list);
        return result;
    }

    public static void main(String[] args) {
        List<Integer> flatList = flatten(List.of(
                List.of(1, 2), List.of(3, 4, 5), List.of(6,7)));
        System.out.println(flatList);
    }
}
