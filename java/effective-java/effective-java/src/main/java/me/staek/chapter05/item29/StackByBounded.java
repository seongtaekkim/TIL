package me.staek.chapter05.item29;


import java.util.Arrays;
import java.util.List;

/**
 *
 *  bytecode에서 Object였던 코드가 Number로 변경된 것을 확인해보자.
 */
public class StackByBounded<E extends Number> {
    private Number[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public StackByBounded() {
        elements = new Number[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0)
            throw new EmptyStackException();
        @SuppressWarnings("unchecked") E result = (E)elements[--size];
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
        StackByBounded<Integer> stack = new StackByBounded<>();
        for (Integer arg : List.of(1, 2, 3))
            stack.push(arg);
        while (!stack.isEmpty())
            System.out.println(stack.pop());
    }
}
