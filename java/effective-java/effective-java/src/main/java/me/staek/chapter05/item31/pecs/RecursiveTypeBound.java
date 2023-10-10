package me.staek.chapter05.item31.pecs;


import java.util.ArrayList;
import java.util.List;


/**
 * 재귀적 타입 한정 : 생성자,소비자 두번 중첩. (<E extends Comparable<? super E>> )
 * - Comparable는 존재하는 정보를 꺼내서 사용하기에 소비자에 속한다. 따라서 <? super E> 를 작성한다.
 * - 아래 예제에서 E 는 IntegerBox 타입인데, Box가 상속한 부모만 Comparable를 구현한 경우에 에러가 발생할 수 있다.
 *     => Box<Integer> max = max(list); 리턴타입이 Integer라는 타입을 정해주면 에러가 나지 않지만
 *     =>  Box<?> max = max(list); 정해지지 않은 타입이라면, 컴파일에러가 발생한다.
 * -
 */
public class RecursiveTypeBound {
    public static <E extends Comparable<? super E>> E max(List<? extends E> list) {
        if (list.isEmpty())
            throw new IllegalArgumentException("빈 리스트");

        E result = null;
        for (E e : list)
            if (result == null || e.compareTo(result) > 0)
                result = e;

        return result;
    }

    public static void main(String[] args) {
        List<IntegerBox> list = new ArrayList<>();
        list.add(new IntegerBox(10, "effective"));
        list.add(new IntegerBox(2, "java"));

        System.out.println(max(list));
//        Box<?> max = max(list); //
        Box<Integer> max = max(list);
        System.out.println(max.toString());
    }
}
