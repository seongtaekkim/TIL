package me.staek.memo.menu;

import me.staek.memo.MemoFrame;

public abstract class FileMenu implements MemoMenu {
    protected MemoFrame memoFrame;
    public FileMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }
    @Override
    public abstract void doItem(String command);

}
