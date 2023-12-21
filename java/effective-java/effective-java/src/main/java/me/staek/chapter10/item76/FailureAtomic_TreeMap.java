package me.staek.chapter10.item76;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * TreeMap put 할 때마다 Comparator 로 데이터를 비교한다.
 * 실패하면 바로 ClassCastException 으로 fail-fast !!
 */
public class FailureAtomic_TreeMap {
    public static void main(String[] args) {
        TreeMap<Integer, Integer> frequencyTable = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        Integer[] arr = {10, 100, 50};
        for (Integer s : arr)
            frequencyTable.put(s, 1);

        Object o = "wrong data";
        try {
            frequencyTable.put((Integer) o, 1);
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(frequencyTable);


    }
}
