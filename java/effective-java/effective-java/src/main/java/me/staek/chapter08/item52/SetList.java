package me.staek.chapter08.item52;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 오토박싱에 의해 radically different 에 혼동이 생긴다.
 * - List.remove(Object)
 * - List.remove(int)
 */
public class SetList {
    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        for (int i = 0; i < 3; i++) {
            set.remove(i);
//            list.remove(i);
            list.remove(Integer.valueOf(i));
        }
        System.out.println(set + " " + list);
    }
}
