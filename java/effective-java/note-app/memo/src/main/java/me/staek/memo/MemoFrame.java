package me.staek.memo;


import me.staek.memo.factory.ActionListenerFactory;
import me.staek.memo.handler.KeyHandler;
import me.staek.memo.code.Menu;
import me.staek.memo.code.MenuType;
import me.staek.memo.code.Program;

import javax.swing.*;
import javax.swing.undo.UndoManager;

public class MemoFrame {

    JFrame frame;
    JTextArea textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenuItem iWrap;

    UndoManager um = new UndoManager();
    boolean wordWarpOn = false;
    KeyHandler keyHandler = new KeyHandler(this);
    private ActionListenerFactory actionListenerFactory;


    private void init() {
        frame = new JFrame(Program.APP_NAME);
        frame.setSize(Program.WITDH,Program.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void start(ActionListenerFactory actionListenerFactory) {
        this.actionListenerFactory = actionListenerFactory;

        init();
        newTextArea();
        newMenu();
//        formatMenu.selectedFont = "Arial";
//        formatMenu.createFont(16);
//        formatMenu.wordWarp();
        frame.setVisible(true);
    }

    private void newMenu() {
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        addMenu(Menu.ROOT, menuBar);
    }

    private void addMenu(Menu menu, JMenuBar jMenuBar) {
        menu.children().stream().forEach((f) -> {
            System.out.println(f.value());
            JMenu jMenu = new JMenu(f.value());
            addItem(f, jMenu);
            jMenuBar.add(jMenu);
        });
    }

    private void addItem(Menu menu, JMenu parent) {
        menu.children().stream().forEach((f) -> {
            if (f.type() == MenuType.MENU) {
                JMenu jMenu = new JMenu(f.value());
                addItem(f, jMenu);
                parent.add(jMenu);
            } else if (f.type() == MenuType.ITEM) {
                JMenuItem item = new JMenuItem(f.value());
                item.addActionListener(actionListenerFactory.actionListener(menu.value()));
                item.setActionCommand(f.value());
                parent.add(item);
            }
        });
    }

    private void newTextArea() {
        textArea = new JTextArea();
        textArea.addKeyListener(keyHandler);
        textArea.getDocument().addUndoableEditListener(e -> um.addEdit(e.getEdit()));

        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);
    }

}
