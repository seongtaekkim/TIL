package me.staek.memo;

import me.staek.memo.factory.ActionListenerFactory;

public class App {
    public static void main(String[] args) throws Exception {

        MemoTextArea textArea = new MemoTextArea();
        MemoFrame memo = new MemoFrame(textArea);
        try (ActionListenerFactory factory = new ActionListenerFactory(memo)) {
            memo.start(factory);
        }
    }
}
