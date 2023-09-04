package me.staek.chapter03.item14;

import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.ToIntFunction;

import static java.util.Comparator.comparingInt;

/**
 * TODO 비교 대상이 여러개일 때 compareTo 전략들
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
     * TODO 대상이 기본형일 경우 중요도 순서에 따라 비교한다.
     *
     */
    @Override
    public int compareTo(PhoneNumberComparatorTest pn) {
        int result = Short.compare(areaCode, pn.areaCode);
        if (result == 0)  {
            result = Short.compare(prefix, pn.prefix);
            if (result == 0)
                result = Short.compare(lineNum, pn.lineNum);
        }
        return result;
    }

    /**
     * TODO Comparator 에 정의된 비교자 생성 메서드를 활용할 수 있다.
     *      - 속도가 비교적 느리긴 하다.
     */
    private static final Comparator<PhoneNumberComparatorTest> COMPARATOR =
            comparingInt((PhoneNumberComparatorTest pn) -> pn.areaCode)
                    .thenComparingInt(new ToIntFunction<PhoneNumberComparatorTest>() {
                        @Override
                        public int applyAsInt(PhoneNumberComparatorTest pn) {
                            return pn.getPrefix();
                        }
                    })
                    .thenComparingInt(pn -> pn.lineNum);
//
//    @Override
//    public int compareTo(PhoneNumber pn) {
//        return COMPARATOR.compare(this, pn);
//    }

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
