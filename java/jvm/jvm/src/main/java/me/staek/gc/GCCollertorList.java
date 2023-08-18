package me.staek.gc;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

public class GCCollertorList {
    public static void main(String[] args) {
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.printf("MemoryManager: %15s",bean.getName() + " , ");
            System.out.println("MemoryPoolNames: " + Arrays.toString(bean.getMemoryPoolNames()));
        }
    }
}
