package me.staek.memo;


import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemoFrame implements ActionListener {

    JFrame frame;
    JTextArea textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenu menuFile, menuEdit, menuFormat, menuColor;
    JMenuItem fileNew, fileOpen, fileSave, fileSaveas, fileExit;

    JMenuItem iWrap, iFontArial, iFontCSMS, iFontTNR,
            iFontSize8, iFontSize12, iFontSize16, iFontSize24;
    JMenu menuFont, menuFontSize;


    JMenuItem undo, redu;
    UndoManager um = new UndoManager();

    boolean wordWarpOn = false;
    FileMenu fileMenu = new FileMenu(this);
    FormatMenu formatMenu = new FormatMenu(this);

    EditMenu editMenu = new EditMenu(this);
    public MemoFrame() {
        newWindow();
        newTextArea();
        newMenu();
        newFormatMenu();
        createEditMenu();
        formatMenu.selectedFont = "Arial";
        formatMenu.createFont(16);
        formatMenu.wordWarp();
        frame.setVisible(true);
    }

    public void createEditMenu() {
        undo = new JMenuItem("Undo");
        undo.addActionListener(this);
        undo.setActionCommand("Undo");
        menuEdit.add(undo);

        redu = new JMenuItem("Redo");
        redu.addActionListener(this);
        redu.setActionCommand("Redo");
        menuEdit.add(redu);
    }

    private void newMenu() {
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        menuFile = new JMenu("file");
        menuEdit = new JMenu("Edit");
        menuFormat = new JMenu("Format");
        menuColor = new JMenu("Color");

        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuFormat);
        menuBar.add(menuColor);

        fileNew = new JMenuItem("new");
        menuFile.add(fileNew);

        fileOpen = new JMenuItem("open");
        menuFile.add(fileOpen);

        fileSave = new JMenuItem("save");
        menuFile.add(fileSave);

        fileSaveas = new JMenuItem("saveas");
        menuFile.add(fileSaveas);

        fileExit = new JMenuItem("exit");
        menuFile.add(fileExit);


        fileNew.addActionListener(this);
        fileNew.setActionCommand("New");
        fileOpen.addActionListener(this);
        fileOpen.setActionCommand("Open");

        fileSave.addActionListener(this);
        fileSave.setActionCommand("Save");

        fileSaveas.addActionListener(this);
        fileSaveas.setActionCommand("SaveAs");

        fileExit.addActionListener(this);
        fileExit.setActionCommand("Exit");
    }

    private void newTextArea() {
        textArea = new JTextArea();


        textArea.getDocument().addUndoableEditListener(
                new UndoableEditListener() {
                    @Override
                    public void undoableEditHappened(UndoableEditEvent e) {
                        um.addEdit(e.getEdit());
                    }
                }
        );

        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);
    }

    private void newWindow() {
        frame = new JFrame("note");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void newFormatMenu() {
        iWrap = new JMenuItem("Word Wrap: Off");
        iWrap.addActionListener(this);
        iWrap.setActionCommand("Word Wrap");
        menuFormat.add(iWrap);

        menuFont = new JMenu("Font");
        menuFormat.add(menuFont);

        iFontArial = new JMenuItem("Arial");
        iFontArial.addActionListener(this);
        iFontArial.setActionCommand("Arial");
        menuFont.add(iFontArial);

        iFontCSMS = new JMenuItem("CSMS");
        iFontCSMS.addActionListener(this);
        iFontCSMS.setActionCommand("CSMS");
        menuFont.add(iFontCSMS);

        iFontTNR = new JMenuItem("TNR");
        iFontTNR.addActionListener(this);
        iFontTNR.setActionCommand("TNR");
        menuFont.add(iFontTNR);

        menuFontSize = new JMenu("FontSize");
        menuFormat.add(menuFontSize);

        iFontSize8 = new JMenuItem("8");
        iFontSize8.addActionListener(this);
        iFontSize8.setActionCommand("size8");
        menuFontSize.add(iFontSize8);

        iFontSize12 = new JMenuItem("12");
        iFontSize12.addActionListener(this);
        iFontSize12.setActionCommand("size12");
        menuFontSize.add(iFontSize12);

        iFontSize16 = new JMenuItem("16");
        iFontSize16.addActionListener(this);
        iFontSize16.setActionCommand("size16");
        menuFontSize.add(iFontSize16);

        iFontSize24 = new JMenuItem("24");
        iFontSize24.addActionListener(this);
        iFontSize24.setActionCommand("size24");
        menuFontSize.add(iFontSize24);
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
            case "size8": formatMenu.createFont(8); break;
            case "size12": formatMenu.createFont(12); break;
            case "size16": formatMenu.createFont(16); break;
            case "size24": formatMenu.createFont(24); break;
        }
    }
}
