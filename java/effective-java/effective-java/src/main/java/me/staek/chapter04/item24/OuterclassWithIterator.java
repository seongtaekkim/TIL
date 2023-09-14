package me.staek.chapter04.item24;

import java.util.AbstractSet;
import java.util.Iterator;

/**
 * member class 정의 시 내부적으로 인스턴스를 생성하는 형식으로 사용할 수 있다.
 */
public class OuterclassWithIterator<E> extends AbstractSet<E> {
    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public int size() {
        return 0;
    }

    private class MyIterator implements Iterator<E> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
    }

}
