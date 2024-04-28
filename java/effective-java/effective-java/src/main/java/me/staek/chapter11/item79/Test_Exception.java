package me.staek.chapter11.item79;

import java.util.HashSet;

/**
 * iterator 순회 중 modcount가 변경되면 .ConcurrentModificationException 가 발생한다.
 *
 */
public class Test_Exception {
    public static void main(String[] args) {
        ObservableSet<Integer> set =
                new ObservableSet<>(new HashSet<>());

        set.addObserver(new SetObserver<>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23)
                    s.removeObserver(this);
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}
