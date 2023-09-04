package me.staek.chapter03.item14.decimal;

import java.math.BigDecimal;

/**
 * TODO 부동소수점 연산은 BigInteger 에서 제공하는 기능이 안전하다.
 */
public class DecimalIsNotCorrect {

    public static void main(String[] args) {
        int i = 1;
        double d = 0.1;
        System.out.println(i - d * 9);

        BigDecimal bd = BigDecimal.valueOf(0.1);
        System.out.println(BigDecimal.valueOf(1).subtract(bd.multiply(BigDecimal.valueOf(9))));
    }
}
