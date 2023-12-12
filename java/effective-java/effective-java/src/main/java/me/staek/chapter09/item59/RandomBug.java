package me.staek.chapter09.item59;

import java.util.Random;

/**
 * 랜덤함수는 만들기 어렵다.
 */
public class RandomBug {
    static Random rnd = new Random();

    static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }

    public static void main(String[] args) {
        int n = 2 * (Integer.MAX_VALUE / 3);
        int low = 0;
        for (int i = 0; i < 1000000; i++)
            if (random(n) < n/2)
                low++;
        System.out.println(low);

        System.out.println(Integer.MIN_VALUE); // -2147483648
        System.out.println(Math.abs(Integer.MIN_VALUE)); // -2147483648 * -1 = -2147483648
        System.out.println(Math.abs(Integer.MIN_VALUE) % 10000); // 음수
    }
}
