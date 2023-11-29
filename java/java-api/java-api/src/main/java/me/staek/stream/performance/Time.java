package me.staek.stream.performance;

public class Time {
    public double measure(IntArrToLongConsumer c, int[] data) {
        long start = System.currentTimeMillis();
        System.out.println("result : " + c.accept(data));;
        long end = System.currentTimeMillis();
        return (double)(end - start)/1000;
    }
}
