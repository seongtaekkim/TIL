package me.staek.chapter07.item42;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparingInt;

/**
 * Collections.sort(List<T> list, Comparator<? super T> c) 의 두번 째 인자 입력 4가지
 */
public class SortFourWays {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("car","apple", "banana");

        /**
         * Comparator 익명클래스
         */
        Collections.sort(words, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });
        System.out.println(words);
        Collections.shuffle(words);

        /**
         * Comparator 람다
         */
        Collections.sort(words,
                (s1, s2) -> Integer.compare(s1.length(), s2.length()));
        System.out.println(words);
        Collections.shuffle(words);

        /**
         * 비교자 생성 메서드 (item14, 43)
         * Comparator.comparingInt
         */
        Collections.sort(words, comparingInt(String::length));
        System.out.println(words);
        Collections.shuffle(words);

        /**
         * List default method
         */
        words.sort(comparingInt(String::length));
        System.out.println(words);
    }
}
