package me.staek.chapter07.item44;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * removeEldestEntry 재정의
 */
public class LinkedHashMap2<K,V>  extends LinkedHashMap {
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > 3;
    }

    public static void main(String[] args) {
        LinkedHashMap2<Integer, String> li_hash =
                new LinkedHashMap2<>();
        li_hash.put(1, "Coding");
        li_hash.put(2, "is");
        li_hash.put(3, "fun");
        li_hash.put(4, "dfef");
        System.out.println("" + li_hash);
    }
}
