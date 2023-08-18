package me.staek.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * javac jvm/src/main/java/me/staek/gc/*.java -d ./
 * java -verbosegc -XX:+UseSerialGC me/staek/gc/GCMonitor
 */
public class GCMonitor {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 0; true ; ++i) {
            if (i % 1000000 == 0) {
                System.out.println("size : " + list.size());
                Thread.sleep(1000);
            }
            PrintFullGC.print();
            list.add(new String ("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        }
    }
}
