package me.staek.chapter05.item31.pecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;

/**
 * 와일드카드 타입을 이용한 생산자,소비자 (PECS) 사용 예제 (main문에서 설명)
 */
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked") 
    public Stack() {
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size==0)
            throw new EmptyStackException();
        E result = elements[--size];
        elements[size] = null;
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }

    /**
     * 와일드카드 타입을 사용하지 않은 pushAll 메서드
     */
//    public void pushAll(Iterable<E> src) {
//        for (E e : src)
//            push(e);
//    }

    /**
     * E 생산자(producer) 매개변수에 와일드카드 타입 적용
     */
    public void pushAll(Iterable<? extends E> src) {
        for (E e : src)
            push(e);
    }



    /**
     * 와일드카드 타입을 사용하지 않은 popAll 메서드
     */
//    public void popAll(Collection<E> dst) {
//        while (!isEmpty())
//            dst.add(pop());
//    }

    /**
     * E 소비자(consumer) 매개변수에 와일드카드 타입 적용
     */
    public void popAll(Collection<? super E> dst) {
        while (!isEmpty())
            dst.add(pop());
    }


    public static void main(String[] args) {
        /**
         * 생산자 예제
         * 불공변이므로 다음은 성립하지 않는다. Iterable<Number> <<-- Iterable<Integer> 따라서 컴파일 에러가 발생한다.
         * 하지만 논리적으로 Integer 타입은 Number 타입으로 추상화하여 사용할 수 있다.
         * 이를 만족시키기 위해 pushAll(Iterable<E> src) ==> pushAll(Iterable<? extends E> src) 이렇게 구성한다.
         * ==> 생산자는 어떤 인자라 입력되었을 때 정보를 쌓아간다는 의미로서, E타입을 상속한 무엇이든 상관 없음을 내포한다.
         */
        Stack<Number> numberStack = new Stack<>();
        Iterable<Integer> integers = Arrays.asList(3, 1, 4, 1, 5, 9);
        numberStack.pushAll(integers);


        Iterable<Double> doubles = Arrays.asList(3.1, 1.0, 4.0, 1.0, 5.0, 9.0);
        numberStack.pushAll(doubles);

        /**
         * 소비자 예제
         * popAll메서드 인자로 전달 될 때 Collection<Number> <<-- Collection<Object> 는 성립되지 않는다
         * 하지만 논리적으로 Number 타입을 꺼내서 Object 타입으로 전달할 의도이기에 문제가 없다.
         * 이를 만족시키기 위해 popAll(Collection<E> dst) ==>> popAll(Collection<? super E> dst) 이렇게 구성한다.
         * ==> 소비자는 이미 존재하는 리소스를 꺼내서 입력된 인자에 넣어주는 경우를 의미하므로, E타입이 상속한 무엇이든 상관 없음을 내포한다.
         */
        Collection<Object> objects = new ArrayList<>();
        numberStack.popAll(objects);

        System.out.println(objects);
    }
}
