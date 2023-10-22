package me.staek.chapter06.item37;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

class _02_Plant {
    enum LifeCycle { ANNUAL, PERENNIAL, BIENNIAL }

    final String name;
    final LifeCycle lifeCycle;

    _02_Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override public String toString() {
        return name;
    }

    public static void main(String[] args) {
        _02_Plant[] garden = {
            new _02_Plant("Basil",    LifeCycle.ANNUAL),
            new _02_Plant("Carroway", LifeCycle.BIENNIAL),
            new _02_Plant("Dill",     LifeCycle.ANNUAL),
            new _02_Plant("Lavendar", LifeCycle.PERENNIAL),
            new _02_Plant("Parsley",  LifeCycle.BIENNIAL),
            new _02_Plant("Rosemary", LifeCycle.PERENNIAL)
        };

        /**
         * EnumMap를 이용한 인덱싱
         */
        Map<LifeCycle, Set<_02_Plant>> plantsByLifeCycle = new EnumMap<>(LifeCycle.class);
        for (LifeCycle lc : LifeCycle.values())
            plantsByLifeCycle.put(lc, new HashSet<>());
        for (_02_Plant p : garden)
            plantsByLifeCycle.get(p.lifeCycle).add(p);
        System.out.println(plantsByLifeCycle);
        System.out.println("=============================");

        /**
         * Stream 을 이용한 인덱싱
         */
        System.out.println(Arrays.stream(garden)
                .collect(groupingBy(p -> p.lifeCycle)));


        System.out.println("=============================");
        /**
         * Sream, EnumMap 을 이용한 인덱싱
         */
        System.out.println(Arrays.stream(garden)
                .collect(groupingBy(p -> p.lifeCycle,
                        () -> new EnumMap<>(LifeCycle.class), toSet())));
    }
}
