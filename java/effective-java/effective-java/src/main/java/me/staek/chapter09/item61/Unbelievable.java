package me.staek.chapter09.item61;

/**
 * Boxing 과 primitive type 의 혼용연산은 Auto Unboxing 되는데,
 * Boxing 객체가 null이면 NPE가 발생함.
 */
public class Unbelievable {
    static Integer i ;

    public static void main(String[] args) {
        if (i == 42)
            System.out.println("Unbelievable");
    }
}
