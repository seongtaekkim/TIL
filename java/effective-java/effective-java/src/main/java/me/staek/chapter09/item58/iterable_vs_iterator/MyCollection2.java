package me.staek.chapter09.item58.iterable_vs_iterator;

import java.util.Iterator;
        import java.util.NoSuchElementException;

/**
 * Iterable 확장 클래스
 */
public class MyCollection2<T> implements Iterable<T> {
    private T[] array;

    public MyCollection2(T[] array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int pos = 0;

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

        };
    }

    /**
     * Iterable 을 확장하면 for-each statement 가능
     */
    public static void main(String[] args) {
        Integer[] ints = new Integer[] { 1, 2, 3 };
        MyCollection2<Integer> collection = new MyCollection2<>(ints);

        Iterator<Integer> it = collection.iterator();
        while (it.hasNext())
            System.out.println(it.next());

        System.out.println("===============");

        for (Integer i: collection)
            System.out.println(i);
    }
}
