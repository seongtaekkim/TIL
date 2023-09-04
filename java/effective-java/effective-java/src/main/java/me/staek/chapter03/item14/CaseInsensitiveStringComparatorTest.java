package me.staek.chapter03.item14;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO 객체 참조 필드가 하나뿐인 비교자
 */
public final class CaseInsensitiveStringComparatorTest
        implements Comparable<CaseInsensitiveStringComparatorTest> {
    private final String s;

    public CaseInsensitiveStringComparatorTest(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // 수정된 equals 메서드 (56쪽)
    @Override public boolean equals(Object o) {
        return o instanceof CaseInsensitiveStringComparatorTest &&
                ((CaseInsensitiveStringComparatorTest) o).s.equalsIgnoreCase(s);
    }

    @Override public int hashCode() {
        return s.hashCode();
    }

    @Override public String toString() {
        return s;
    }

    /**
     * TODO 자바가 제공하는 비교자를 사용해 클래스를 비교할 수 있다.
     * @param cis the object to be compared.
     * @return
     */
    public int compareTo(CaseInsensitiveStringComparatorTest cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }

    public static void main(String[] args) {
        Set<CaseInsensitiveStringComparatorTest> s = new TreeSet<>();
        for (String arg : args)
            s.add(new CaseInsensitiveStringComparatorTest(arg));
        System.out.println(s);
    }
}
