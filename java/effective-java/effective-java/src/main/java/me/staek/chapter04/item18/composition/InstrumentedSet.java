package me.staek.chapter04.item18.composition;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  TODO ForwardingSet의 위임을 통해 Set을 composition을 한다. 위임을 하기에 decorator pattern 을 사용하고 있기도 하다.
 *       decorator pattern
 *       -> Component: Set
 *          Decorator: ForwardingSet
 *          ContreteDecorator: InstrumentedSet
 *
 *
 *  TODO InstrumentedSet는 Set기능을 사용하기 위해
 *       Set으로 요청을 위임해주는 ForwardingSet class를 상속받았다.
 *       이전예제 (InstrumentedHashSet.java)와 같은 요청이지만, extends 를 하지 않았기때문에
 *       side effect없이 정상 동작한다.
 *
 *  TODO HashSet 에 기능이 추가변경 되더라도 Set 엔 영향이 없다.
 *       Set에 기능이 추가된다면, 컴파일이 안될 것이기에 (Override) 문제가 없다.
 *
 */
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public InstrumentedSet(Set<E> s) {
        super(s);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
    public int getAddCount() {
        return addCount;
    }

    public static void main(String[] args) {
        InstrumentedSet<String> s = new InstrumentedSet<>(new HashSet<>());
        s.addAll(List.of("apple", "banana", "cherry"));
        System.out.println(s.getAddCount());
    }
}
