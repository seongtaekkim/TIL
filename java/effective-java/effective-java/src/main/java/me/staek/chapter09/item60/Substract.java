package me.staek.chapter09.item60;

import java.math.BigDecimal;

/**
 * double, float의 연산은 근사치를 출력해주므로 정확하지 않을 수 있다.
 * 이를 보정하기 위해 BigDecimal , int, long 등을 이용한다.
 */
public class Substract {
    public static void main(String[] args) {
        System.out.println(1.03 - 0.42);
        System.out.println(1.00 - 9 * 0.10);

        System.out.println(BigDecimal.valueOf(1.03).subtract(BigDecimal.valueOf(0.42)));
        System.out.println(BigDecimal.valueOf(1.00).subtract(BigDecimal.valueOf(9).multiply(BigDecimal.valueOf(0.10))));
    }
}
