package me.staek.chapter02.item14.comparable_and_comparator;

import java.util.Arrays;
import java.util.Comparator;

/**
 * TODO 원하는 라인에서 Comparator의 compare 메서드를 이용해서 기본 정렬인 compareTo를 사용하지 않고
 *      새롭게 정렬을 구할 수 있다.
 */
public class ComparableEx {
    public static void main(String[] args) {
        String[] names = {"55" , "8222", "3", "6"};
        Arrays.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.compareTo(o2) > 0)
                    return (1);
                return (-1);
            }
        });
        System.out.println(Arrays.toString(names));
    }
}
