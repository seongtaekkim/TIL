package me.staek.chapter05.item31.pecs;

import java.util.HashSet;
import java.util.Set;

/**
 * T 생산자 매개변수에 와일드카드 타입 적용
 */
public class Union {

    /**
     * 생산자이므로 <? extends E> 를 작성한다.
     */
    public static <E> Set<E> union(Set<? extends E> s1,
                                   Set<? extends E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    public static void main(String[] args) {
        Set<Integer> integers = new HashSet<>();
        integers.add(1); 
        integers.add(3); 
        integers.add(5); 

        Set<Double> doubles =  new HashSet<>();
        doubles.add(2.0); 
        doubles.add(4.0); 
        doubles.add(6.0); 

        Set<Number> numbers = union(integers, doubles);
//      Set<Number> numbers = Union.<Number>union(integers, doubles); // 타입추론 until java7

        System.out.println(numbers);
    }
}
