package me.staek.chapter04.item21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ConcurrentModificationException
 * - HashMap, Collection 등에서 Concurrency 지원을 안하는 자료구조에서는 iterator 중에 데이터를 변경할 시 예외가 발생한다.
 *
 * 회피방법
 * 1. 자료구조에서 iterator를 가져와 iterator에서 직접 변경해 준다.
 * 2. iterator말고 index로 순회하다가 변경해 준다.
 * 3. Collection 구현체일 경우, removeIf default method 를 이용한다.
 */
public class AvoidConcurrentModificationException {
    public static void main(String[] args) {

        /**
         * Hashmap
         */
        HashMap<PhoneNumber, String> map = new HashMap<PhoneNumber, String>();
        map.put(new PhoneNumber(1,1,1), "one");
        map.put(new PhoneNumber(2,2,2), "two");

        /**
         * iterator 중에 직접 삭제하면 예외 발생
         */
//        Iterator<PhoneNumber> it = map.keySet().iterator();
//        map.remove(new PhoneNumber(2,2,2));
//        while (it.hasNext()) {
//            PhoneNumber key = it.next();
//        }

        /**
         * iterator가 삭제하면 정상진행.
         */
//        for (Iterator<PhoneNumber> iterator = map.keySet().iterator(); iterator.hasNext();) {
//            PhoneNumber phoneNumber= iterator.next();
//            if (phoneNumber.getLineNum() == 2)
//                iterator.remove();
//            System.out.println(phoneNumber.getPrefix());
//        }

        /**
         * 인덱스로 순회하며 삭제하면 정상진행
         */
//        for (int i = 0; i < map.size() ; i++) {
//            if (map.get(new PhoneNumber(1,1,1)) != null) {
//                map.remove(new PhoneNumber(1,1,1));
//            }
//        }
//        map.keySet().stream().forEach(System.out::println);


        /**
         * Collection 을 구현한 ArrayList
         * - iterator가 직접 삭제하거나 인덱스를 이용한다. (hashmap 참고)
         */
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);

        /**
         * iterator 중에 자료구조에서 직접 remove 하면 예외 발생
         */
//        for (Integer number : numbers) {
//            if (number == 3) {
//                numbers.remove(number);
//            }
//        }

        /**
         * Collection 의 removeIf 사용
         */
//        numbers.removeIf(number -> number == 3);
//        numbers.stream().forEach(System.out::println);
    }
}
