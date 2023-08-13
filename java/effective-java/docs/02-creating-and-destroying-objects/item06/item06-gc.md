# GC



### memory leak test

~~~java
/**
 * JVM - memory leak test
 */
public class JvmGcTest {
    public static void main( String[] args ) throws InterruptedException {
        Thread.sleep(10000);

        MyThread m[]; // = new MyThread()[2];
        int size = 1;
        m = new MyThread[size];
        for (int i = 0 ; i < size ; i++) {
            m[i] = new MyThread();
            m[i].start();
        }
    }
    static class MyThread extends Thread {
        @Override
        public void run() {
            GenericStack<Object0> st = new GenericStack();
            while(true) {
                st.push(new Object0());
            }
        }
    }
}

~~~



~~~java
class Object0 {
    private Integer data[][][][] = null;
    Object0() {
        //data = new Integer[100][100][100][100];
        data = new Integer[20000][20000][20000][20000];
    }
}
~~~



~~~java
class GenericStack<E> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public GenericStack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if(size == 0) {
            throw new EmptyStackException();
        }

        @SuppressWarnings("unchecked")  E result = (E) elements[--size];
        elements[size] = null; // reference release
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if(elements.length == size)
            elements = Arrays.copyOf(elements, 2*size+1);
    }
}
~~~



![스크린샷 2023-08-13 오후 2.31.19](../../../img/item06-01.png)







### refrence

[gc 개념](https://www.youtube.com/watch?v=FMUpVA0Vvjw)

[jstat](https://utoi.tistory.com/entry/jstat-JVM-%ED%86%B5%EA%B3%84-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EA%B0%90%EC%8B%9C-%ED%88%B4)





