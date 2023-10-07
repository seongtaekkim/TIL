package me.staek.memo.handler;

import me.staek.memo.menu.MemoMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu Item 이벤트 처리
 */
public class MemoActionHandler implements ActionListener {

    private final MemoMenu memoMenu;

    public MemoActionHandler(MemoMenu memoMenu) {
        this.memoMenu = memoMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        this.memoMenu.doItem(command);
    }
}
