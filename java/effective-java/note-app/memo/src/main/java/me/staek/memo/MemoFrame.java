package me.staek.memo;


import me.staek.memo.menuenum.Menu;
import me.staek.memo.menuenum.MenuType;

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
    MemoMenu fileMenu = new FileMenu(this);
    MemoMenu formatMenu = new FormatMenu(this);
    MemoMenu  editMenu = new EditMenu(this);
    KeyHandler  keyHandler = new KeyHandler(this);

    MemoActionHandler listener1;
    MemoActionHandler listener2;
    MemoActionHandler listener3;
    private void init() {
        frame = new JFrame("note");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MemoFrame() {
        init();
        listener1 = new MemoActionHandler(fileMenu);
        listener2 = new MemoActionHandler(formatMenu);
        listener3 = new MemoActionHandler(editMenu);
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
                if (menu.value() == "File")
                    item.addActionListener(listener1);
                if (menu.value() == "Font" || menu.value() == "FontSize")
                    item.addActionListener(listener2);
                if (menu.value() == "Edit")
                    item.addActionListener(listener3);
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
