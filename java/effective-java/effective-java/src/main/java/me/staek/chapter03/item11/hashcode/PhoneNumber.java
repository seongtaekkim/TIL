package me.staek.chapter03.item11.hashcode;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO 여러가지 해시코드 메서드를 구현한 class
 */
public final class PhoneNumber {
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

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "area code");
        this.prefix   = rangeCheck(prefix,   999, "prefix");
        this.lineNum  = rangeCheck(lineNum, 9999, "line num");
    }

    private static short rangeCheck(int val, int max, String arg) {
        return (short) val;
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber))
            return false;
        PhoneNumber pn = (PhoneNumber)o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }


    // 같은 해시코드만 리턴.
    @Override
    public int hashCode() {
        return 42;
    }

    // 전형적인 해시코드
//    @Override public int hashCode() {
//        int result = Short.hashCode(areaCode); // 1
//        result = 31 * result + Short.hashCode(prefix); // 2
//        result = 31 * result + Short.hashCode(lineNum); // 3
//        return result;
//    }

    // Intellij hashcode
//    @Override public int hashCode() {
//        return Objects.hash(lineNum, prefix, areaCode);
//    }

    // 불변객체 해시코드 지연초기화 + 스레드안정성
//    private volatile int hashCode; // 자동으로 0으로 초기화된다.

//    @Override public int hashCode() {
//        if (this.hashCode != 0) {
//            return hashCode;
//        }
//
//        synchronized (this) {
//            int result = hashCode;
//            if (result == 0) {
//                result = Short.hashCode(areaCode);
//                result = 31 * result + Short.hashCode(prefix);
//                result = 31 * result + Short.hashCode(lineNum);
//                this.hashCode = result;
//            }
//            return result;
//        }
//    }

    public static void main(String[] args) {
        Map<PhoneNumber, String> map = new HashMap<>();

        PhoneNumber number1 = new PhoneNumber(123, 456, 7890);
        PhoneNumber number2 = new PhoneNumber(123, 456, 7890);

        System.out.println(number1.equals(number2));
        System.out.println(number1.hashCode());
        System.out.println(number2.hashCode());

        map.put(number1, "spring");
        map.put(number2, "java");

        String s1 = map.get(number2);
        System.out.println(s1);
        String s2 = map.get(new PhoneNumber(123, 456, 7890));
        System.out.println(s2);
    }
}
