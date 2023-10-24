package me.staek.chapter02.item05.supplier;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class LazyEvaluation {

    public static void main(String[] args) {
        printValueIndex(1, ()->getValue());
        printValueIndex(0, ()->getValue());
        printValueIndex(-2, ()->getValue());
    }

    private static void printValueIndex(int i, Supplier<String> s) {
        if (i > 0)
            System.out.println("value is " + s.get());
        else
            System.out.println("false");
    }

    private static String getValue()  {
        try {
            TimeUnit.SECONDS.sleep(2);
            return "True";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
