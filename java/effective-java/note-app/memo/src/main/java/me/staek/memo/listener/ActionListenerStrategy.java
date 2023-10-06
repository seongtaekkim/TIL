package me.staek.memo.listener;


import me.staek.memo.MemoMenu;

import java.awt.event.ActionListener;

public interface ActionListenerStrategy {
    String name();
    ActionListener createListener(MemoMenu memoMenu);
}
