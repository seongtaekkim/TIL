package me.staek.memo.listener;


import me.staek.memo.handler.MemoActionHandler;
import me.staek.memo.MemoMenu;
import me.staek.memo.code.Menu;

import java.awt.event.ActionListener;

public class FontMenuListener implements ActionListenerStrategy {

    private final String name = Menu.FONT.value();
    @Override
    public String name() {
        return this.name;
    }
    @Override
    public ActionListener createListener(MemoMenu menu) {
        return new MemoActionHandler(menu);
    }
}
