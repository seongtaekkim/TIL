package me.staek.chapter07.item43;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.*;

/**
 * 메소드 참조 예제 5가지
 */
public class _02_MethodReferenceEx {
    public static void main(String[] args) {

        /**
         * static method reference
         */
        UnaryOperator<String> hi = _02_Greeting::hi;
        System.out.println(hi.apply("name"));

        /**
         * instance method reference (bound)
         */
        _02_Greeting greeting = new _02_Greeting();
        UnaryOperator<String> hello = greeting::hello;
        System.out.println(hello.apply("name2"));

        /**
         * instance method reference (unbound)
         * - 임의객체
         */
        String[] names = {"d", "b", "c"};
        Arrays.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 0;
            }
        });
        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));

        /**
         * 클래스 생성자
         */
        Supplier<_02_Greeting> newGreeting = _02_Greeting::new;
        System.out.println(newGreeting.get().getName());

        Function<String, _02_Greeting> staekGreeting = _02_Greeting::new;
        System.out.println(staekGreeting.apply("staek").getName());

        /**
         * 배열생성자 레퍼런스
         */
        IntFunction<String[]> generator = String[]::new;
        String[] apply = generator.apply(5); // {null, null ...}

        // 사용 예
        ArrayList<String> list = new ArrayList<>();
        list.add("Peter");
        list.add("Paul");
        list.add("Mary");
        String[] toArray = list.stream().toArray(String[]::new);
//        String[] toArray = list.stream().toArray(new IntFunction<String[]>() {
//            @Override
//            public String[] apply(int value) {
//                return new String[value];
//            }
//        });
        System.out.println(Arrays.toString(toArray));


    }
}
