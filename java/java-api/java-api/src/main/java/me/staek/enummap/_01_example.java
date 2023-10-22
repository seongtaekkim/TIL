package me.staek.enummap;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class _01_example {
    enum DayOfWeek {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    public static void main(String[] args) {
        EnumMap<DayOfWeek, String> scheduleMap = new EnumMap<>(DayOfWeek.class);
        scheduleMap.put(DayOfWeek.MON, "Study");
        System.out.println(scheduleMap.get(DayOfWeek.MON));;


        /**
         * 동기화를 위해서는 아래처럼 래핑해주어야 한다.
         */
        Map<DayOfWeek, String> synchMap = Collections.synchronizedMap(scheduleMap);
        System.out.println(synchMap.get(DayOfWeek.MON));;
    }
}
