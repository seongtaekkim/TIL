package me.staek.chapter07.item44;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * LinkedHashMap 인스턴스에 독립적인 함수객체를 인자로 주어 remove 메서드를 수행한다.
 * - 커스텀 함수형 인터페이스 EldestEntryRemovalFunction
 * - 표준 함수형 인터페이스 BiPredicate
 *
 */
class RemoveMap {
    public static boolean remove(Map<Integer, String> map, Map.Entry<Integer, String> eldest) {
        if (map.size() > 3) {
            map.remove(eldest.getKey());
            return true;
        }
        return false;
    }
}
public class LinkedHashMapWithMethodReference<K,V>  extends LinkedHashMap<K,V> {

    // 커스텀 함수형 인터페이스 EldestEntryRemovalFunction 사용
//    public void remove(EldestEntryRemovalFunction<K, V> ev) {
//        if (this.size() == 0)
//            throw new IllegalStateException();
//        ev.remove(this, this.entrySet().stream().findFirst().get());
//    }
    // 표준 함수형 인터페이스 BiPredicate 사용
    public void remove(BiPredicate<Map<K, V>, Map.Entry<K, V>> ev) {
        if (this.size() == 0)
            throw new IllegalStateException();
        ev.test(this, this.entrySet().stream().findFirst().get());
    }

    public static void main(String[] args) {

        LinkedHashMapWithMethodReference<Integer, String> li_hash = new LinkedHashMapWithMethodReference<>();
        li_hash.put(1, "Coding");   li_hash.put(2, "is");   li_hash.put(3, "fun");  li_hash.put(4, "!!!!");

        li_hash.remove(RemoveMap::remove);
        System.out.println(li_hash);
    }
}
