package me.staek.memo;


import me.staek.memo.factory.ActionListenerFactory;
import me.staek.memo.handler.KeyHandler;
import me.staek.memo.code.Menu;
import me.staek.memo.code.MenuType;
import me.staek.memo.code.Program;
import me.staek.memo.view.MemoTextArea;

import javax.swing.*;

public class MemoFrame {

    private JFrame frame;
    private final MemoTextArea textArea;
    private ActionListenerFactory actionListenerFactory;

    public MemoFrame(MemoTextArea textArea) {
        this.textArea = textArea;
    }

    public MemoTextArea textArea() {
        return textArea;
    }
    public JFrame frame() {
        return this.frame;
    }

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
        frame.setVisible(true);
    }

    private void newMenu() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        addMenu(Menu.ROOT, menuBar);
    }

    private void addMenu(Menu menu, JMenuBar jMenuBar) {
        menu.children().stream().forEach((f) -> {
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
        textArea.addKeyListener(new KeyHandler(this));
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);
    }
}
