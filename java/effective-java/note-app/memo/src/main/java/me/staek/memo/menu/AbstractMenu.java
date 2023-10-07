package me.staek.memo.menu;

import me.staek.memo.MemoFrame;

public abstract class AbstractMenu implements MemoMenu {
    protected MemoFrame memoFrame;
    public AbstractMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }
    @Override
    public abstract void doItem(String command);

}
