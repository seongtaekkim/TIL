package me.staek.memo;

import me.staek.memo.factory.ActionListenerFactory;

import java.io.File;

public class App {
    public static void main(String[] args) {
        // setting
        File file = new File("cfg");
        if (!file.exists()) {
            file.mkdir();
        }

        // program 실행
        MemoTextArea textArea = new MemoTextArea();
        MemoFrame memo = new MemoFrame(textArea);
        try (ActionListenerFactory factory = new ActionListenerFactory(memo)) {
            memo.start(factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
