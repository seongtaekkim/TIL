package me.staek.chapter07.item44;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * removeEldestEntry 재정의
 */

public class LinkedHashMap2<K,V>  extends LinkedHashMap {

    public void remove(EldestEntryRemovalFunction<Integer, String> ev) {
        if (this.size() == 0)
            throw new ArrayStoreException();
        System.out.println("1111222");
        Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) this.entrySet().iterator().next();
        ev.remove(this, entry);
    }

    public static void main(String[] args) {

        LinkedHashMap2<Integer, String> li_hash =
                new LinkedHashMap2<>();
        li_hash.put(1, "Coding");
        li_hash.put(2, "is");
        li_hash.put(3, "fun");
        li_hash.put(4, "dfef");

        li_hash.remove(new EldestEntryRemovalFunction<Integer, String>() {
            @Override
            public boolean remove(Map<Integer, String> map, Map.Entry<Integer, String> eldest) {
                if (map.size() > 3) {
                    System.out.println("1111");
                    map.remove(eldest.getKey());
                    return true;
                }
                return false;
            }
        });
        System.out.println("" + li_hash);

    }
}
