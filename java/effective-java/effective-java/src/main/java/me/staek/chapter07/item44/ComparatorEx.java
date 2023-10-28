package me.staek.chapter07.item44;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.ToIntBiFunction;

public class ComparatorEx {
    public static void main(String[] args) {
        String[] names = {"d", "b", "c"};
        Arrays.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
//        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));
    }
}
