package me.staek.chapter09.item58.custom_iterable;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 사용자가 관리하는 임의의 데이터는 Iterable 를 확장하여 반복자로 쉽게 접근할 수 있다.
 */
public class EffectiveClass<E> implements Iterable<E> {

    private E[] elementData;
    private int size;
    private int defaultSize = 2;

    @SuppressWarnings("unchecked")
    public EffectiveClass() {
        elementData = (E[]) new Object[defaultSize];
    }

    public void add(E element) {
        ensureCapacity();
        elementData[size++] = element;
    }

    private void ensureCapacity() {
        int capacity = this.elementData.length;
        if (size >= capacity) {
            int newCapacity = capacity << 1;
            this.elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ClassIterator();
    }

    public class ClassIterator implements Iterator<E> {
        int pos;
        int lastReturned = -1;

        public boolean hasNext() {
            return pos != size;
        }

        public E next() {
            int current = pos;
            E[] elements = EffectiveClass.this.elementData;
            pos = current + 1;
            lastReturned = current;
            return elements[lastReturned];
        }
    }
}
