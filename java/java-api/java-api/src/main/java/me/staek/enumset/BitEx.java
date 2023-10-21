package me.staek.enumset;

/**
 * https://park-youjin.tistory.com/17
 *
 * RegularEnumSet 비트연산 테스트
 */
public class BitEx {
    enum test {
        a,b,c,d,e
    }

    public static void main(String[] args) {
        long elements = 0L;
        test[] universe = test.class.getEnumConstants();


//        System.out.println(Long.toBinaryString(~0L));
//        System.out.println(Long.toBinaryString(-1L)); // ~0L 하고 같음
//        System.out.println(Long.toBinaryString(~0L >>> 1));
//        System.out.println(Long.toBinaryString(-1L >>> 2));
//        System.out.println(Long.toBinaryString(-1L >>> -1));
//        System.out.println(Long.toBinaryString(-1L >>> -3));

        // addAll
        elements = -1L >>> -universe.length;
        System.out.println(Long.toBinaryString(elements));

//        System.out.println(Long.toBinaryString(1L << universe[2].ordinal())); // 1 을 왼쪽으로 2번 민다 : 100
//        System.out.println(Long.toBinaryString(~(1L << universe[2].ordinal()))); // 100 Not 연산. (64비트)
//        System.out.println(Long.toBinaryString(( elements & ~(1L << universe[2].ordinal())))); // element와 AND 연산
        // remove
        elements &= ~(1L << universe[2].ordinal());
        System.out.println(Long.toBinaryString(elements));

//        System.out.println(Long.toBinaryString(elements));
//        System.out.println(Long.toBinaryString(~elements));
        // removeAll
        elements &= ~elements;
        System.out.println(Long.toBinaryString(elements));
    }
}
