package me.staek.chapter05.item29;

import java.util.Arrays;
import java.util.List;

/**
 * 아이템 7 stack 예제
 * 데이터를 꺼낼 때 (String)stack.pop()).toUpperCase() 와 같이
 * 명시적 캐스팅을 해주어야하고, 잘못 캐스팅할 경우 runtime exception 이 발생한다.
 *
 */
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }

    public static void main(String[] args) {
        Stack stack = new Stack();
        for (String arg : List.of("a", "b", "c"))
            stack.push(arg);
        while (!stack.isEmpty())
            System.out.println(((String)stack.pop()).toUpperCase());
    }
}
