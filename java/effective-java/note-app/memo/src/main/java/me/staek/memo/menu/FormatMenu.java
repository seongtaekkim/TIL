package me.staek.memo.menu;


import me.staek.memo.MemoFrame;
import me.staek.memo.MemoMenu;

public abstract class FormatMenu implements MemoMenu {
    protected MemoFrame memoFrame;
    public FormatMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }

    @Override
    public abstract void doItem(String command);

}
