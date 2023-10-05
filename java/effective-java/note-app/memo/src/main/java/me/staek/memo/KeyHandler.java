package me.staek.memo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    MemoFrame memoFrame;
    public KeyHandler(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        this.memoFrame = memoFrame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            memoFrame.fileMenu.save();
        }
        if (e.isShiftDown() && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            memoFrame.fileMenu.saveAs();
        }
        if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_F) {
            memoFrame.menuFile.doClick();
        }

        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            memoFrame.editMenu.undo();
        }
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            memoFrame.editMenu.redo();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
