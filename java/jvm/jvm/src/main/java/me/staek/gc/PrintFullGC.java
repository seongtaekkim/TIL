package me.staek.gc;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PrintFullGC {
    static String oldGenGcName ="";
    static Map<String, Long> gcStatMap = new HashMap<String, Long>();

    public static void print() {
        long fullGcDelta = 0;
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            String beanName = bean.getName();
            long newCount = bean.getCollectionCount();
            if (beanName.equals(oldGenGcName)) {
                long oldCount = gcStatMap.get(beanName);
                fullGcDelta = newCount - oldCount;
            }
            gcStatMap.put(beanName, newCount);
        }
        findOldGenGC();
        if (fullGcDelta > 0) {
            System.out.print(red("staek : "));
            System.out.println(gcStatMap + " OldGenGC is [" + oldGenGcName + "] FULL GC #" + fullGcDelta);
        }
    }

    private static void findOldGenGC() {
        if ("".equals(oldGenGcName) == false)
            return ;
        String foundName = "";
        long minGcCount = Long.MAX_VALUE;
        for (String gcName : gcStatMap.keySet()) {
            long gcCount = gcStatMap.get(gcName);
            if (gcCount < minGcCount) {
                foundName = gcName;
                minGcCount = gcCount;
            } else if (gcCount == minGcCount) {
                foundName = "";
            }
        }
        if ("".equals(foundName) == false) {
            System.out.print(red("staek : "));
            System.out.println("Found OldGenGC=" + foundName);
        }
        oldGenGcName = foundName;
     }

     /*
      * // Color codes
      * // black   30
      * // red     31
      * // green   32
      * // yellow  33
      * // blue    34
      * // magenta 35
      * // cyan    36
      * // white   37
      */
     private static String red(String s) {
        return "\u001B[31m" + s + "\u001B[0m";
     }
}
