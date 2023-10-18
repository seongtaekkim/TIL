package me.staek.chapter06.item40;

import java.util.HashSet;
import java.util.Set;

/**
 * @Override 가 없을 때 발생할 수 있는 문제
 * - equals()를 override 한 줄 알 았으나 인자를 잘못 작성해, 사실은 overloading 한게 되었다!?
 * - HashSet 사용 시 의도와 다르게 결과가 나옴.
 */
public class _01_Bigram {
    private final char first;
    private final char second;

    public _01_Bigram(char first, char second) {
        this.first  = first;
        this.second = second;
    }

    public boolean equals(_01_Bigram b) {
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    /**
     * 예상 : 26
     * 결과 : 260
     */
    public static void main(String[] args) {
        Set<_01_Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new _01_Bigram(ch, ch));
        System.out.println(s.size());
    }
}
