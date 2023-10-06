package me.staek.memo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu Item 이벤트 처리
 */
public class MemoActionListener implements ActionListener {

    FileMenu fileMenu;
    FormatMenu formatMenu;
    EditMenu  editMenu;

    public MemoActionListener(FileMenu fileMenu, FormatMenu formatMenu, EditMenu editMenu) {
        this.fileMenu = fileMenu;
        this.formatMenu = formatMenu;
        this.editMenu = editMenu;
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
