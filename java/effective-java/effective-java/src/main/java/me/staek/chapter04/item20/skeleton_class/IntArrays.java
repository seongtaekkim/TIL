package me.staek.chapter04.item20.skeleton_class;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * List의 스켈레톤 클래스인 AbstractList 덕분에, 새로운 class를 정의할 때
 * 개발자가 정의해야 할 메서드가 현저히 적어지는 장점이 있다.
 */
public class IntArrays {
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);

        return new AbstractList<>() {
            @Override public Integer get(int i) {
                return a[i];
            }

            @Override public Integer set(int i, Integer val) {
                int oldVal = a[i];
                a[i] = val;
                return oldVal;
            }

            @Override public int size() {
                return a.length;
            }
        };
    }

    public static void main(String[] args) {
        int[] a = new int[10];
        for (int i = 0; i < a.length; i++)
            a[i] = i;

        List<Integer> list = intArrayAsList(a);
        Collections.shuffle(list);
        System.out.println(list);
    }
}
