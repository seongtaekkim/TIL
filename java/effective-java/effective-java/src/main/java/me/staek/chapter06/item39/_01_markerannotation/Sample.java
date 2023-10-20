package me.staek.chapter06.item39._01_markerannotation;


import me.staek.chapter06.item39.Test;

public class Sample {
    @Test
    public static void m1() { }
    public static void m2() { }
    @Test
    public static void m3() {    // Test should fail
        throw new RuntimeException("Boom");
    }
    public static void m4() { }
//    @Test
    public void m5() { }
    public static void m6() { }
    @Test
    public static void m7() {    // Test should fail
        throw new RuntimeException("Crash");
    }
    public static void m8() { }
}
