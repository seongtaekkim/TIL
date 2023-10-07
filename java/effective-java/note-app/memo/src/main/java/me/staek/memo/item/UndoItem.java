package me.staek.memo.item;

import me.staek.memo.menu.EditMenu;
import me.staek.memo.MemoFrame;
import me.staek.memo.code.Menu;

import javax.swing.undo.UndoManager;

/**
 * Undo manage class
 */
public class UndoItem extends EditMenu {
    UndoManager um;
    public UndoItem(MemoFrame memoFrame) {
        super(memoFrame);
        registUndoListner();
    }

    private void registUndoListner() {
        um = new UndoManager();
        memoFrame.textArea().getDocument().addUndoableEditListener(e -> um.addEdit(e.getEdit()));
    }

    @Override
    public void doItem(String command) {
        if (Menu.UNDO.value().equals(command))
            um.undo();
        else if (Menu.REDO.value().equals(command))
            um.redo();
        // else 예외처리 추가
    }
}
