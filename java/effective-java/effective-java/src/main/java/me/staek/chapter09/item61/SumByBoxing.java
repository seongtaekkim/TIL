package me.staek.chapter09.item61;


/**
 *
 * Boxing 과 Unboxing 을 반복적으로 수행하여 실행시간이 굉장히 길어짐을 알 수 있다.
 *
 * Boxing : new Long(..);
 * UnBoxing : Long.longValue();
 */
public class SumByBoxing {
    static Long sum = 0L;
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        for (int i=0 ; i<Integer.MAX_VALUE ; i++) {
//            sum  = Long.valueOf(sum.longValue() + i);  // 박싱 + 언박싱
//            sum += i; // 박싱 + 언박싱
            sum = 322L; // 박싱 + 언박싱
//            sum = 3L; // 캐싱데이터 로딩 비용만 든다.
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start)/1000);
        System.out.println(sum);
    }
}
