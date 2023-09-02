package me.staek.chapter03.item11.hashcode_test;

import me.staek.chapter03.item11.hashcode.PhoneNumber;

import java.util.HashMap;


/**
 * TODO PhoneNumber 인스턴스의 hashcode가 같은값만 존재하는 경우
 *      HashMap, HashTable 성능을 테스트 해보자.
 *      (PhoneNumber의 hashcode() return 값을 상수로 고정하자)
 */
public class HashcodeStressTest {
    public static void main(String[] args) {
        int LIMIT = Integer.MAX_VALUE / 10000;
        System.out.println("개수: " + LIMIT);

        HashMap<PhoneNumber, Integer> hashMap = new HashMap<>(LIMIT * 2 );
//        Hashtable<PhoneNumber, Integer> hashMap = new Hashtable<>();

        long beforeTime = System.currentTimeMillis();
        for (int i = 0; i < LIMIT ; i++) {
            hashMap.put(new PhoneNumber(i, 456, 7890), i);
        }
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime);
        System.out.println("생성시간 : "+secDiffTime);


        beforeTime = System.currentTimeMillis();

        Integer i = hashMap.get(new PhoneNumber(100, 456, 7890));
        System.out.println("데이터: " + i);

        afterTime = System.currentTimeMillis();
        secDiffTime = (afterTime - beforeTime);
        System.out.println("조회시간 : "+secDiffTime);
    }
}
