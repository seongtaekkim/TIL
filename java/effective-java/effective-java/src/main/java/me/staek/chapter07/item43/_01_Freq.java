package me.staek.chapter07.item43;

import java.util.Map;
import java.util.TreeMap;

/**
 * 람다, 메소드참조 예제
 */
public class _01_Freq {
    public static void main(String[] args) {
        Map<String, Integer> frequencyTable = new TreeMap<>();
        String[] arr = {"10", "100"};
        for (String s : arr)
            frequencyTable.merge(s, 1, (a, b) -> Integer.sum(a, b)); // Lambda
        System.out.println(frequencyTable);

        frequencyTable.clear();
        for (String s : arr)
            frequencyTable.merge(s, 1, _01_MyInteger::sum); // Method reference
        System.out.println(frequencyTable);

    }
}
