package me.staek.memo;


import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemoFrame implements ActionListener {

    JFrame frame;
    JTextArea textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenu menuFile, menuEdit, menuFormat, menuColor;
    JMenuItem fileNew, fileOpen, fileSave, fileSaveas, fileExit;




    FileMenu fileMenu = new FileMenu(this);

    public MemoFrame() {
        newWindow();
        newTextArea();
        newMenu();


        frame.setVisible(true);
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


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch(command) {
            case "New": fileMenu.newFile(); break;
            case "Open": fileMenu.open(); break;
            case "Save": fileMenu.save(); break;
            case "SaveAs": fileMenu.saveAs(); break;
            case "Exit": fileMenu.exit(); break;
        }
    }
}
