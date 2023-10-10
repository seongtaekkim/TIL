package me.staek.chapter05.item31.typeinference;

import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Comparator.comparingInt;

/**
 * 타입추론 한계 (item14 예제)
 *  private static final Comparator<PhoneNumberComparatorTest> COMPARATOR 정의 참고
 *
 */
public final class PhoneNumberComparatorTest implements Cloneable, Comparable<PhoneNumberComparatorTest> {
    private final short areaCode, prefix, lineNum;

    public short getAreaCode() {
        return areaCode;
    }

    public short getPrefix() {
        return prefix;
    }

    public short getLineNum() {
        return lineNum;
    }

    public PhoneNumberComparatorTest(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "지역코드");
        this.prefix   = rangeCheck(prefix,   999, "프리픽스");
        this.lineNum  = rangeCheck(lineNum, 9999, "가입자 번호");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val;
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumberComparatorTest))
            return false;
        PhoneNumberComparatorTest pn = (PhoneNumberComparatorTest)o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }

    @Override public int hashCode() {
        int result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        return result;
    }


    @Override public String toString() {
        return String.format("%03d-%03d-%04d",
                areaCode, prefix, lineNum);
    }

    /**
     * TODO 타입추론 한계
     * - comparingInt함수 인자에 명시적형변환을 해야한다. 이후 체이닝 메서드에서는 안해도 된다.
     * - comparingInt함수 인자는 Consumer 여서 정확히 어떤 상위타입인지 추론이 불가능하기에 지정해주어야 하는 듯하다.
     */
    private static final Comparator<PhoneNumberComparatorTest> COMPARATOR =
            comparingInt((PhoneNumberComparatorTest pn) -> pn.areaCode)
                    .thenComparingInt(pn -> pn.getPrefix())
                    .thenComparingInt(pn -> pn.lineNum);

    @Override
    public int compareTo(PhoneNumberComparatorTest pn) {
        return COMPARATOR.compare(this, pn);
    }

    private static PhoneNumberComparatorTest randomPhoneNumber() {
        Random rnd = ThreadLocalRandom.current();
        return new PhoneNumberComparatorTest((short) rnd.nextInt(1000),
                               (short) rnd.nextInt(1000),
                               (short) rnd.nextInt(10000));
    }

    public static void main(String[] args) {
        Set<PhoneNumberComparatorTest> s = new TreeSet<>();
        for (int i = 0; i < 10; i++)
            s.add(randomPhoneNumber());
        System.out.println(s);
    }

}
