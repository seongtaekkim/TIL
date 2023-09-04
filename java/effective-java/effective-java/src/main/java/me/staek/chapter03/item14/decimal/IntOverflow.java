package me.staek.chapter03.item14.decimal;

/**
 * TODO primitive type 은 wrapping class 에서 제공하는 compare 함수사용이 안전하다.
 */
public class IntOverflow {

    public static void main(String[] args) {
        System.out.println(-2147483648);
        System.out.println(-2147483648 - 10);
        System.out.println(Integer.compare(-2147483648, 10));

    }
}
