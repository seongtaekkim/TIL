package me.staek.chapter05.item31.pecs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * T 생산자 매개변수에 와일드카드 타입 적용
 */
public class Chooser<T> {
    private final List<T> choiceList;
    private final Random rnd = new Random();

    /**
     * 생산자이기 때문에 <? extends T> 형태로 작성한다.
     */
    public Chooser(Collection<? extends T> choices) {
        choiceList = new ArrayList<>(choices);
    }

    public T choose() {
        return choiceList.get(rnd.nextInt(choiceList.size()));
    }

    public static void main(String[] args) {
        List<Integer> intList = List.of(1, 2, 3, 4, 5, 6);
        Chooser<Number> chooser = new Chooser<>(intList);
        for (int i = 0; i < 10; i++) {
            Number choice = chooser.choose();
            System.out.println(choice);
        }
    }
}
