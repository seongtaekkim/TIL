package me.staek.chapter09.item58.iterable_vs_iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator 확장 클래스
 */
public class MyCollection1<T> implements Iterator<T> {
    private T[] array;
    private int pos;

    public MyCollection1(T[] array) {
        this.array = array;
    }

    public Iterator<T> iterator() {
        pos = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return (pos < array.length && array[pos] != null);
    }

    /**
     * @throws NoSuchElementException
     */
    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        return array[pos++];
    }

    /**
     * 반복자를 이용해 출력은 가능하지만 for-each statement는 사용하지 못한다.
     */
    public static void main(String[] args) {

        Integer[] ints = new Integer[] { 1, 2, 3 };
        MyCollection1<Integer> collection = new MyCollection1<>(ints);

        Iterator<Integer> it = collection.iterator();
        while (it.hasNext())
            System.out.println(it.next());
    }
}
