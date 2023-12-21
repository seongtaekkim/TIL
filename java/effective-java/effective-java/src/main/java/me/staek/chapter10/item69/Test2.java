package me.staek.chapter10.item69;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class Foo {
    int cnt;
    public Foo(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "cnt=" + cnt +
                '}';
    }
}

/**
 * API설계가 잘못된 케이스
 * - 상태의존적 메서드(next())는 상태검사메서드(hasNext())와 같이 제공되어야 한다.
 *
 * 1번예제 시간) 48312
 * 2번예제 시간) 3482
 */
public class Test2 {
    public static void main(String[] args) throws InterruptedException {

        List<Foo> list = new ArrayList<>();
        list.add(new Foo(1));
        list.add(new Foo(2));

        /**
         * 1번 예제
         */
        long start = System.nanoTime();
        try {
            Iterator<Foo> i = list.iterator();
            while (true) {
                Foo foo = i.next();
//                System.out.println(foo);
            }
        } catch(NoSuchElementException e) {
        }
        long end = System.nanoTime();
        System.out.println((end-start));

        /**
         * 2번 예제
         */
        start = System.nanoTime();
        for (Iterator<Foo> i = list.iterator() ; i.hasNext() ;) {
            Foo foo = i.next();
//            System.out.println(foo);
        }
        end = System.nanoTime();
        System.out.println((end-start));
    }
}
