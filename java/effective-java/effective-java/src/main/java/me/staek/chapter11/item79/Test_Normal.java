package me.staek.chapter11.item79;

import java.util.HashSet;

public class Test_Normal {
    public static void main(String[] args) {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());

        set.addObserver(new SetObserver<Integer>() {
            @Override
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}
