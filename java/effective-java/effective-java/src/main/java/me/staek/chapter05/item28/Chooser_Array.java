package me.staek.chapter05.item28;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chooser_Array {
    private final Object[] choiceList;

    public Chooser_Array(Collection choices) {
        choiceList = choices.toArray();
    }

    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceList[rnd.nextInt(choiceList.length)];
    }

    public static void main(String[] args) {
//        List<Integer> intList = List.of(1, 2, 3, 4, 5, 6);
        List<String> intList = List.of("aa","bb");
        Chooser_Array chooser = new Chooser_Array(intList);

        for (int i = 0; i < 10; i++) {
            Number choice = (Number) chooser.choose();
            System.out.println(choice);
        }
    }
}
