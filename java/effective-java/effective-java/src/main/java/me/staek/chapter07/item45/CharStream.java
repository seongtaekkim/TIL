package me.staek.chapter07.item45;

/**
 * "Hello world!".chars() : IntStream data.
 */
public class CharStream {
    public static void main(String[] args) {
        "Hello world!".chars().forEach(System.out::print);
        System.out.println();

        System.out.println("Hello world!".chars().toArray()[0]);

        // 명시적 형변환을 해야 문자열이 출력됨
        "Hello world!".chars().forEach(x -> System.out.print((char) x));
        System.out.println();
    }
}
