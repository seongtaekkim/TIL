package me.staek.chapter09.item61;

import java.util.Comparator;

public class BrokenComparator {
    public static void main(String[] args) {

        /**
         * > < 의 경우, UnBoxing 후 값을 비교한다. (intValue() 가 호출되어 비교함)
         * == 의 경우, 객체 식별을 검사한다.
         */
        Comparator<Integer> naturalOrder =
                (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);


        /**
         * UnBoxking 후 primitive type으로 비교하면 생각한 대로 된다
         */
//        Comparator<Integer> naturalOrder = (iBoxed, jBoxed) -> {
//            int i = iBoxed, j = jBoxed;
//            return i < j ? -1 : (i == j ? 0 : 1);
//        };

//        int result = naturalOrder.compare(Integer.valueOf(142), Integer.valueOf(142));
        int result = naturalOrder.compare(new Integer(42), new Integer(43)); // new Integer() 보다는 캐싱을 사용하는 valueOf를 사용하자.
        System.out.println(result);
    }
}
