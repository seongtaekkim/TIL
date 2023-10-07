package me.staek.memo.listener;


import me.staek.memo.menu.MemoMenu;
import me.staek.memo.code.Menu;
import me.staek.memo.handler.MemoActionHandler;

import java.awt.event.ActionListener;

public class WrapMenuListener implements ActionListenerStrategy {

    private final String name = Menu.FORMAT.value();
    @Override
    public String name() {
        return this.name;
    }
    @Override
    public ActionListener createListener(MemoMenu menu) {
        return new MemoActionHandler(menu);
    }
}
