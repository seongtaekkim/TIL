package me.staek.chapter05.item27;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 내가 이미 알고잇는 예외를 제외해야 다른 예외가 낫을때 파악이 빠름
 */
public class SuppressWarningsTest {

    private int size;

    Object[] elements;

    public SuppressWarningsTest() {
        elements = new Object[100];
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] result = (T[]) Arrays.copyOf(elements, size, a.getClass());
            return result;
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    public static void main(String[] args) {
        SuppressWarningsTest listVo = new SuppressWarningsTest();
        String[] strings = new String[] {"a","b"};
        String[] array = listVo.toArray(strings);

        // 원래는 ArrayList 의 method 이다.
        ArrayList<String[]> arrayList = new ArrayList<>();
        arrayList.toArray(strings);
    }

}
