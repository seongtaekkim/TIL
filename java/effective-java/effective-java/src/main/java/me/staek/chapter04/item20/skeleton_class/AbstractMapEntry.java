package me.staek.chapter04.item20.skeleton_class;

import java.util.Map;
import java.util.Objects;

/**
 * Map.Entry 에 선언한 일반규약 메서드를 모두 재정의하였다.
 * setValue의 경우엔, 사용할경우만 재정의를 하게끔 유도하였다.
 */
public abstract class AbstractMapEntry<K,V> implements Map.Entry<K,V> {
    // 변경 가능한 엔트리는 이 메서드를 반드시 재정의해야 한다.
    @Override public V setValue(V value) {
        throw new UnsupportedOperationException();
    }
    
    // Map.Entry.equals의 일반 규약을 구현한다.
    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Map.Entry))
            return false;
        Map.Entry<?,?> e = (Map.Entry) o;
        return Objects.equals(e.getKey(),   getKey())
                && Objects.equals(e.getValue(), getValue());
    }

    // Map.Entry.hashCode의 일반 규약을 구현한다.
    @Override public int hashCode() {
        return Objects.hashCode(getKey())
                ^ Objects.hashCode(getValue());
    }

    @Override public String toString() {
        return getKey() + "=" + getValue();
    }
}
