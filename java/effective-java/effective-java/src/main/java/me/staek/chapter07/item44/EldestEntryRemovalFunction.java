package me.staek.chapter07.item44;

import java.util.Map;

/**
 * LinkedHashMap 정적팩터리 제공시 함수객체 인자로 사용 가능
 */
@FunctionalInterface
public interface EldestEntryRemovalFunction<K,V> {
    boolean remove(Map<K,V> map, Map.Entry<K,V> eldest);
}
