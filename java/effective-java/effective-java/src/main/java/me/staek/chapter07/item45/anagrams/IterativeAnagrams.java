package me.staek.chapter07.item45.anagrams;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 *
 * Anagram 을 구하는 예제.
 * - 정렬된 문자열이 같은 경우 group으로 묶는다.
 * - group의 키는 정렬된 문자열로 한다.
 *
 * example
 * [aelpp : {apple, pplea, pleap, leapp, eappl}]
 *
 */
public class IterativeAnagrams {
    public static void main(String[] args) throws IOException {
        File dictionary = new File("item45-testfile");
        System.out.println(dictionary.getAbsolutePath());
        int minGroupSize = Integer.parseInt("1");

        Map<String, Set<String>> groups = new HashMap<>();
        try (Scanner s = new Scanner(dictionary)) {
            while (s.hasNext()) {
                String word = s.next();

                /**
                 * computeIfAbsent 두번째 인자 Function 의 첫번째 인자는 사용하지 않았다.
                 */
                groups.computeIfAbsent(alphabetize(word),
                        new Function<String, Set<String>>() {
                            @Override
                            public Set<String> apply(String unused) {
                                return new TreeSet<>();
                            }
                        }).add(word);
            }
        }
//        for (String group : groups.keySet())
//            System.out.println(group);

        for (Set<String> group : groups.values())
            if (group.size() >= minGroupSize)
                System.out.println(group.size() + ": " + group );
    }

    /**
     * 입력된 문자열을 오름차순으로 정렬 후 리턴
     */
    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
