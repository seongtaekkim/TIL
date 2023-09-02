package me.staek.chapter03.item11.concurrency_collection;

import me.staek.chapter03.item11.hashcode.PhoneNumber;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO  ConcurrentHashMap의 읽기쓰기 테스트.
 *      - iterator를 호출할 때 동시성 보장 등 없이 조회한다.
 *          -> 쓰기에서 동시성을 보장하기에 읽게되는데, 중간에 데이터가 변경되었을 시,
 *             iterator 이전순서라면 조회가 안되고, 나중 순서라면 조회가 된다. (weakly consistent iterator)
 *      - read: 그냥 읽는다.
 *      - write: 같은 버킷에 넣을경우 synchronize 하고, 새로운 데이터를 넣을 경우 CAS 를 사용해 원자성을 보장한다.
 *
 *      https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/concurrent/package-summary.html
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {

        ConcurrentHashMap<PhoneNumber, String> table3 = new ConcurrentHashMap<PhoneNumber, String>();
        table3.put(new PhoneNumber(1,1,1), "one");
        table3.put(new PhoneNumber(2,2,2), "two");
        table3.put(new PhoneNumber(6,6,6), "six");

        Iterator<PhoneNumber> it3 = table3.keySet().iterator();

        table3.remove(new PhoneNumber(2,2,2));

        while (it3.hasNext()) {
            PhoneNumber key = it3.next();
            table3.put(new PhoneNumber(4,4,4), "four");
            System.out.println(key.getLineNum());
        }
    }
}
