package me.staek.memo.menu;

import me.staek.memo.MemoFrame;

public abstract class EditMenu implements MemoMenu {
    protected MemoFrame memoFrame;
    public EditMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }

    @Override
    public abstract void doItem(String command);
}
