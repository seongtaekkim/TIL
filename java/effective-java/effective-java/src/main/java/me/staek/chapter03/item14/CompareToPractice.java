package me.staek.chapter03.item14;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * TODO compareTo 기본 테스트
 *      compareTo 규약 테스트 : 반사성, 대칭성, 추이성, 일관성
 *      equals 와 compareTo 결과는 같아야 하지만, 예외도 존재한다 (BigDecimal)
 */
public class CompareToPractice {

    public static void main(String[] args) {
        BigDecimal n1 = BigDecimal.valueOf(23134134);
        BigDecimal n2 = BigDecimal.valueOf(11231230);
        BigDecimal n3 = BigDecimal.valueOf(53534552);
        BigDecimal n4 = BigDecimal.valueOf(11231230);

        // 반사성
        System.out.println(n1.compareTo(n1));

        // 대칭성
        System.out.println(n1.compareTo(n2));
        System.out.println(n2.compareTo(n1));

        // 추이성
        System.out.println(n3.compareTo(n1) > 0);
        System.out.println(n1.compareTo(n2) > 0);
        System.out.println(n3.compareTo(n2) > 0);

        // 일관성
        System.out.println(n4.compareTo(n2));
        System.out.println(n2.compareTo(n1));
        System.out.println(n4.compareTo(n1));


        // compareTo가 0이라면 equals는 true여야 한다. (BigDecimal처럼 예외도 존재함)
        BigDecimal oneZero = new BigDecimal("1.0");
        BigDecimal oneZeroZero = new BigDecimal("1.00");
        System.out.println(oneZero.compareTo(oneZeroZero)); // Tree, TreeMap // 0
        System.out.println(oneZero.equals(oneZeroZero)); // 순서가 없는 콜렉션 // false

        // Tree, TreeMap은 compareTo로 비교해서 데이터를 입력하기 때문에 1개 데이터만 입력된다,
        TreeSet<BigDecimal> a = new TreeSet<>();
        a.add(oneZero);
        a.add(oneZeroZero);
        System.out.println(a.size());
        Arrays.stream(a.toArray()).forEach(System.out::println);

        // 순서없는 컬렉션 : equals로 비교하기 때문에 2개 모두 입력된다.
        HashSet<BigDecimal> b = new HashSet<>();
        b.add(oneZero);
        b.add(oneZeroZero);
        System.out.println(b.size() + " " );
        Arrays.stream(b.toArray()).forEach(System.out::println);

    }
}
