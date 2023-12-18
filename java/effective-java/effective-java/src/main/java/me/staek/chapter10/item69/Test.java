package me.staek.chapter10.item69;

class Mountain {
    public void climb() {
    }
}

/**
 * 반복문 경계검사를 checked exception 으로 처리하는 예시의 문제점
 * 
 * 1번예제 시간) 62569
 * 2번예제 시간) 8255
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        Mountain[] range = new Mountain[100];
        for (int i=0 ; i<100 ; i++)
            range[i] = new Mountain();

        /**
         * 1번 예제
         */
        long start = System.nanoTime();
        try {
            int i=0;
            while (true)
                range[i++].climb();
        } catch(ArrayIndexOutOfBoundsException e) {
        }
        long end = System.nanoTime();
        System.out.println((end-start));

        /**
         * 2번 예제
         */
        start = System.nanoTime();
        for (Mountain m : range) {
            m.climb();
        }
        end = System.nanoTime();
        System.out.println((end-start));
    }
}
