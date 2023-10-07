package me.staek.memo;


public abstract class FormatMenu implements MemoMenu {
    MemoFrame memoFrame;
    public FormatMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }

    @Override
    public abstract void doItem(String command);

}
