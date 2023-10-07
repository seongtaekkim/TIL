package me.staek.memo;

import me.staek.memo.code.Menu;

public abstract class EditMenu implements MemoMenu {
    MemoFrame memoFrame;
    public EditMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }

    @Override
    public abstract void doItem(String command);
}
