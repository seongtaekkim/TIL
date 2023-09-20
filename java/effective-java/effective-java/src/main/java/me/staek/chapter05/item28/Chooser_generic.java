package me.staek.chapter05.item28;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Chooser_generic<T> {
    private final T[] choiceList;

    //@SuppressWarnings("unchecked")
    public Chooser_generic(Collection<T> choices) {
        choiceList = (T[]) choices.toArray();
    }

    public T choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceList[rnd.nextInt(choiceList.length)];
    }

    public static void main(String[] args) {
//        List<Integer> intList = List.of(1, 2, 3, 4, 5, 6);
        List<String> intList = List.of("aa","bb");

        Chooser_generic chooser = new Chooser_generic<>(intList);

        for (int i = 0; i < 10; i++) {
            Number choice = (Number) chooser.choose();
            System.out.println(choice);
        }
    }
}
