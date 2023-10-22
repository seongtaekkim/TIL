package me.staek.chapter06.item37;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.*;

class _01_Plant {
    enum LifeCycle { ANNUAL, PERENNIAL, BIENNIAL }

    final String name;
    final LifeCycle lifeCycle;

    _01_Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override public String toString() {
        return name;
    }

    public static void main(String[] args) {
        _01_Plant[] garden = {
            new _01_Plant("Basil",    LifeCycle.ANNUAL),
            new _01_Plant("Carroway", LifeCycle.BIENNIAL),
            new _01_Plant("Dill",     LifeCycle.ANNUAL),
            new _01_Plant("Lavendar", LifeCycle.PERENNIAL),
            new _01_Plant("Parsley",  LifeCycle.BIENNIAL),
            new _01_Plant("Rosemary", LifeCycle.PERENNIAL)
        };

        /**
         * Set 배열을 이용한 인덱싱
         */
        @SuppressWarnings("unchecked")
        Set<_01_Plant>[] plantsByLifeCycleArr =
                (Set<_01_Plant>[]) new Set[LifeCycle.values().length];
        for (int i = 0; i < plantsByLifeCycleArr.length; i++)
            plantsByLifeCycleArr[i] = new HashSet<>();
        for (_01_Plant p : garden)
            plantsByLifeCycleArr[p.lifeCycle.ordinal()].add(p);
        // Print the results
        for (int i = 0; i < plantsByLifeCycleArr.length; i++) {
            System.out.printf("%s: %s%n", LifeCycle.values()[i], plantsByLifeCycleArr[i]);
        }
    }
}
