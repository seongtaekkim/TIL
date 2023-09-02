package me.staek.chapter03.item11.concurrency_collection;
import me.staek.chapter03.item11.hashcode.PhoneNumber;
import java.util.HashMap;
import java.util.Iterator;


public class HashMapTest {
    public static void main(String[] args) {

        HashMap<PhoneNumber, String> map = new HashMap<PhoneNumber, String>();
        map.put(new PhoneNumber(1,1,1), "one");
        map.put(new PhoneNumber(2,2,2), "two");

        Iterator<PhoneNumber> it = map.keySet().iterator();

        map.remove(new PhoneNumber(2,2,2));

        while (it.hasNext()) {
            PhoneNumber key = it.next();
        }
    }
}
