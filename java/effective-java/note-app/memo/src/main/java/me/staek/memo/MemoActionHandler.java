package me.staek.memo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu Item 이벤트 처리
 */
public class MemoActionHandler implements ActionListener {

    FileMenu fileMenu;
    FormatMenu formatMenu;
    EditMenu  editMenu;

    MemoMenu memoMenu;

    public MemoActionHandler(MemoMenu memoMenu) {
        this.memoMenu = memoMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        this.memoMenu.doItem(command);
    }
}
