package me.staek.chapter05.item26;

import java.util.ArrayList;
import java.util.List;

/**
 * rawtype사용: 제네릭 타입으로 정의한 class를 사용할 때 매개변수화 타입으로 사용하지 않는 경우.
 * - List를 예시로, add에 Object를 받는다.
 * - 출력시 type casting을 해서 꺼낼 수 있는데(list는 캐스팅 안해도 됨) 잘못 캐스팅하면 런타임에러가 발생한다.
 *
 * generic type
 * - 매개변수타입 으로만 add할 수 있다. (그렇지 않으면 compile error)
 * - 꺼낼 때 엄한 타입으로 casting 하면 compile error
 *
 */
public class RawtypeTest {

    public static void main(String[] args) {
        // raw type
        List raw = new ArrayList();
        raw.add(10);
        raw.add("effective");

        for (Object o: raw) {
//            System.out.println((Integer) o);
            System.out.println(o);
        }

        // generic type
        List<Integer> generics = new ArrayList<>();
        generics.add(10);
//        generics.add("whiteship");

        for (Integer g: generics) {
            System.out.println(g);
        }
    }
}
