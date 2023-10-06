package me.staek.memo;


import me.staek.memo.menuenum.Menu;
import me.staek.memo.menuenum.MenuType;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemoFrame implements ActionListener {

    JFrame frame;
    JTextArea textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenuItem iWrap;


    UndoManager um = new UndoManager();
    boolean wordWarpOn = false;
    FileMenu fileMenu;
    FormatMenu formatMenu;
    EditMenu editMenu;
    KeyHandler keyHandler;

    private void method() {
         fileMenu = new FileMenu(this);
         formatMenu = new FormatMenu(this);
         editMenu = new EditMenu(this);
         keyHandler = new KeyHandler(this);
    }

    private void init() {
        frame = new JFrame("note");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MemoFrame() {
        init();
        newTextArea();
        newMenu();
//        formatMenu.selectedFont = "Arial";
//        formatMenu.createFont(16);
//        formatMenu.wordWarp();
        method();
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
                item.addActionListener(this);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch(command) {
            case "New": fileMenu.newFile(); break;
            case "Open": fileMenu.open(); break;
            case "Save": fileMenu.save(); break;
            case "SaveAs": fileMenu.saveAs(); break;
            case "Exit": fileMenu.exit(); break;
            case "Undo": editMenu.undo(); break;
            case "Redo": editMenu.redo(); break;
            case "Arial": formatMenu.setFont(command); break;
            case "CSMS": formatMenu.setFont(command); break;
            case "TNR": formatMenu.setFont(command); break;
            case "Word Wrap": formatMenu.wordWarp(); break;
            case "Size8": formatMenu.createFont(8); break;
            case "Size12": formatMenu.createFont(12); break;
            case "Size16": formatMenu.createFont(16); break;
            case "Size24": formatMenu.createFont(24); break;
        }
    }
}
