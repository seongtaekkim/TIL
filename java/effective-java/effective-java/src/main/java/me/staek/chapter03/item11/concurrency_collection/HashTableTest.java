package me.staek.chapter03.item11.concurrency_collection;

import me.staek.chapter03.item11.hashcode.PhoneNumber;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * TODO Hashtable iterator 테스트
 *      - Iterator 를 얻고 읽기전에 데이터를 변경하면 ConcurrentModificationException 발생함
 *      - Enumeration 를 받을 수 있는데, 이때에는 데이터를 읽을 수 있고, 데이터변경은 보장하지 않는다.
 */
public class HashTableTest {
    public static void main(String[] args) {
        // Fail-fast
        Hashtable<PhoneNumber, String> table = new Hashtable<PhoneNumber, String>();
        table.put(new PhoneNumber(1,1,1), "one");
        table.put(new PhoneNumber(2,2,2), "two");

        Iterator<PhoneNumber> it = table.keySet().iterator();

//        table.remove(new PhoneNumber(2,2,2));

        while (it.hasNext()) {
            PhoneNumber key = it.next();
        }

        //) Not Fail Fast
        Hashtable<PhoneNumber, String> table2 = new Hashtable<PhoneNumber, String>();
        table2.put(new PhoneNumber(1,1,1), "one");
        table2.put(new PhoneNumber(2,2,2), "two");

        Enumeration<PhoneNumber> enumKey = table2.keys();
        table2.remove(new PhoneNumber(2,2,2));
        while (enumKey.hasMoreElements()) {
            PhoneNumber key = enumKey.nextElement();
            System.out.println(key.getLineNum());
        }

    }
}
