package me.staek.chapter07.item45.anagrams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 *
 * 필요한 로직을 stream과 함수로 역할을 나누어 처리함
 * - StreamAnagrams와 다르게 stream 로직 가독성이 좋다.
 *
 */
public class HybridAnagrams {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get("item45-testfile");
        int minGroupSize = Integer.parseInt("1");

        try (Stream<String> words = Files.lines(dictionary)) {
            words.collect(groupingBy(word -> alphabetize(word)))
                    .values().stream()
                    .filter(group -> group.size() >= minGroupSize)
                    .forEach(g -> System.out.println(g.size() + ": " + g));
        }
    }

    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
