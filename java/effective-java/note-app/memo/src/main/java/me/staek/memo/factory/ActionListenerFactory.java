package me.staek.memo.factory;


import me.staek.memo.*;
import me.staek.memo.listener.*;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ActionListenerFactory implements AutoCloseable {

    private final MemoFrame frame;
    private final Map<String, ActionListener> menuMap = new HashMap<>();
    public ActionListener actionListener(String menu) {
        return menuMap.get(menu);
    }

    public ActionListenerFactory(MemoFrame frame) {
        this.frame = frame;

        ActionListenerStrategy fl = new FileMenuListener();
        menuMap.put(fl.name(), fl.createListener(new FileMenu(frame)));

        ActionListenerStrategy wl = new WrapMenuListener();
        menuMap.put(wl.name(), wl.createListener(new WordWrapItem(frame)));

        ActionListenerStrategy fl2 = new FontMenuListener();
        menuMap.put(fl2.name(), fl2.createListener(new FontItem(frame)));

        ActionListenerStrategy fsl = new FontSizeMenuListener();
        menuMap.put(fsl.name(), fsl.createListener(new FontItem(frame)));

        ActionListenerStrategy el = new EditMenuListener();
        menuMap.put(el.name(), el.createListener(new UndoItem(frame)));
    }

    @Override
    public void close() throws Exception {
        menuMap.clear();
    }
}
