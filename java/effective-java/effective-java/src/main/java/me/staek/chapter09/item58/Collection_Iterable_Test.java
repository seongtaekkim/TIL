package me.staek.chapter09.item58;

import java.util.*;
import java.util.stream.Stream;

/**
 * Iterable 을 확장한 Collection class 테스트
 */
public class Collection_Iterable_Test {
    public static <T> Iterable<T> iterableOf(Stream<T> stream) {
        return stream::iterator;
    }
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();

        list.add("apple");
        list.add("banana");

        /**
         * stream to iterable test
         */
        Stream<Integer> stream = Arrays.stream(new Integer[]{1});
        for (Integer i : iterableOf(stream)) {
            System.out.println(i);
        }

        /**
         * 배열을 Iterable 을 익명클래스로 확장하여 구현함
         */
        int[] aa = new int[] {1,2,3,4,5};
        Iterable<Integer> it2 = new Iterable<>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private int index = 0;
                    @Override
                    public boolean hasNext() {
                        return aa.length > index;
                    }

                    @Override
                    public Integer next() {
                        return aa[index++];
                    }
                };
            }
        };
        for (Integer i : it2) {
            System.out.println(i);
        }

        /**
         * EnumSet for-each test
         */
        enum Day {MON, TWO};
        EnumSet<Day> enumSet = EnumSet.allOf(Day.class);
        for (Day d  : enumSet) {
            System.out.println(d);
        }

        /**
         * HashMap for-each test
         */
        HashMap<String,String> map = new HashMap();
        for (String s : map.keySet()) {
            System.out.println(s);
        }
    }
}
