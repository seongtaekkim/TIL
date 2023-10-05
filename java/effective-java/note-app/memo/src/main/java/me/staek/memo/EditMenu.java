package me.staek.memo;

public class EditMenu {
    MemoFrame memoFrame;
    public EditMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }

    public void undo() {
        memoFrame.um.undo();
    }
    public void redo() {
        memoFrame.um.redo();
    }
}
