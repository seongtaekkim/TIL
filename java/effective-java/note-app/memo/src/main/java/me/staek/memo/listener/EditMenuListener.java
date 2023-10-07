package me.staek.memo.listener;


import me.staek.memo.handler.MemoActionHandler;
import me.staek.memo.menu.MemoMenu;
import me.staek.memo.code.Menu;

import java.awt.event.ActionListener;

public class EditMenuListener implements ActionListenerStrategy {

    private final String name = Menu.EDIT.value();
    @Override
    public String name() {
        return this.name;
    }
    @Override
    public ActionListener createListener(MemoMenu menu) {
        return new MemoActionHandler(menu);
    }
}
