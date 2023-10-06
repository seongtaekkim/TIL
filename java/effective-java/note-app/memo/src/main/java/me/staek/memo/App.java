package me.staek.memo;

import me.staek.memo.factory.ActionListenerFactory;

public class App {
    public static void main(String[] args) throws Exception {
        MemoFrame memo = new MemoFrame();
        try (ActionListenerFactory factory = new ActionListenerFactory(memo)) {
            memo.start(factory);
        }
    }
}
