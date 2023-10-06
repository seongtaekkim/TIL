package me.staek.memo;

import me.staek.memo.menuenum.Menu;

public class EditMenu implements MemoMenu {
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

    @Override
    public void doItem(String command) {
        if (Menu.UNDO.value().equals(command)) {
            undo();
        } else if (Menu.REDO.value().equals(command)) {
            redo();
        }
    }
}
