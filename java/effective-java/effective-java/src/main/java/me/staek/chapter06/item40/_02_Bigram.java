package me.staek.chapter06.item40;

import java.util.HashSet;
import java.util.Set;

/**
 * equals 메서드에 @Override 를 붙여서 오타 등을 예방할 수 있다.
 */
public class _02_Bigram {
    private final char first;
    private final char second;

    public _02_Bigram(char first, char second) {
        this.first  = first;
        this.second = second;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof _02_Bigram))
            return false;
        _02_Bigram b = (_02_Bigram) o;
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        Set<_02_Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new _02_Bigram(ch, ch));
        System.out.println(s.size());
    }
}
