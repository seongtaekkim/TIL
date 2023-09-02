package me.staek.chapter03.item11.concurrency_collection;

import me.staek.chapter03.item11.hashcode.PhoneNumber;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * TODO 쓰기, Iterator 등을 시작할 때 데이터를 copy한다.
 *      - iterator를 호출할 때에도 copy 배열을 가져와 순회하며 조회할 수 있다 (fail-safe)
 *      - read: copy 데이터를 읽기 때문에 동기화할 필요가 없고, 여러 스레드가 동시에 접근해도 된다.
 *          -> iterator 중간에 데이터가 변경되어도 반영되지 않는다.
 *      - write: copy 진행 후 데이터를 변경하기에 속도가 느리다.
 */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {
        
        CopyOnWriteArrayList<PhoneNumber> list = new CopyOnWriteArrayList<PhoneNumber>();

        list.add(new PhoneNumber(1,1,1));
        list.add(new PhoneNumber(2,2,2));
        list.add(new PhoneNumber(6,6,6));

        Iterator<PhoneNumber> it4 = list.iterator();
        list.remove(new PhoneNumber(2,2,2));

        while (it4.hasNext()) {
            list.add(new PhoneNumber(8,8,8));
            PhoneNumber key = it4.next();
            System.out.println(key.getLineNum());
        }
    }
}
