package me.staek.memo;

public class WordWrapItem extends FormatMenu {
    boolean onoffWrap = false;
    public WordWrapItem(MemoFrame memoFrame) {
        super(memoFrame);
    }

    public void wordWarp() {
        if (onoffWrap == false) {
            onoffWrap = true;
            memoFrame.textArea.setLineWrap(true);
            memoFrame.textArea.setWrapStyleWord(true);
        } else if (onoffWrap == true) {
            onoffWrap = false;
            memoFrame.textArea.setLineWrap(false);
            memoFrame.textArea.setWrapStyleWord(false);
        }
    }

    @Override
    public void doItem(String command) {
        wordWarp();
    }
}
