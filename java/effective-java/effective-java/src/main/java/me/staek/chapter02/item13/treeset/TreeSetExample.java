package me.staek.chapter02.item13.treeset;


import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO TreeSet in/out test
 */
public class TreeSetExample {

    static class View implements Comparable<View> {
        int age;
        View(int age) {
            this.age = age;
        }
        int getAge() {
            return this.age;
        }

        @Override
        public int compareTo(View o) {
            return Integer.compare(this.age, o.getAge());
        }
    }

    public static void main(String[] args) {

        System.out.println("기본타입 test =============================================");
        TreeSet<Integer> treeSet1 = new TreeSet<>();
        treeSet1.add(10);
        treeSet1.add(4);
        treeSet1.add(6);
        for (int number : treeSet1) {
            System.out.println(number);
        }

        System.out.println("comparable test =============================================");
        /**
         * TODO 인자에 인스턴스가 Comparable을 미구현할 경우, ClassCastException 발생
         */
        TreeSet<View> treeSet2 = new TreeSet<>();
        treeSet2.add(new View(10));
        treeSet2.add(new View(1000));
        treeSet2.add(new View(100));

        for (View number : treeSet2) {
            System.out.println(number.getAge());
        }


        System.out.println("comparator test =============================================");
        /**
         * TODO Comparable 미구현일 경우, Comparator를 제공하면 동작한다.
         */
        TreeSet<View> numbers = new TreeSet<>(Comparator.comparingInt(View::hashCode));
        /**
         * TODO synchronizedSet으로 Thread-safe 하게 동작한다.
         */
        Set<View> phoneNumbers = Collections.synchronizedSet(numbers);
        phoneNumbers.add(new View(10));
        phoneNumbers.add(new View(1000));
        phoneNumbers.add(new View(100));

        for (View number : numbers) {
            System.out.println(number.getAge());
        }
    }
}
