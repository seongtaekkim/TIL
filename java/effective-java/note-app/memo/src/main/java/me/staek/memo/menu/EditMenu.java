package me.staek.memo.menu;

import me.staek.memo.MemoFrame;
import me.staek.memo.MemoMenu;
import me.staek.memo.code.Menu;

public abstract class EditMenu implements MemoMenu {
    protected MemoFrame memoFrame;
    public EditMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }

    @Override
    public abstract void doItem(String command);
}
