package me.staek.memo;

import java.awt.*;

public class FormatMenu {
    MemoFrame memoFrame;
    Font arial, csms, tnr;
    String selectedFont;
    public FormatMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }


    public void wordWarp() {
        if (memoFrame.wordWarpOn == false) {
            memoFrame.wordWarpOn = true;
            memoFrame.textArea.setLineWrap(true);
            memoFrame.textArea.setWrapStyleWord(true);
            memoFrame.iWrap.setText("Word Wrap: On");
        } else if (memoFrame.wordWarpOn == true) {
            memoFrame.wordWarpOn = false;
            memoFrame.textArea.setLineWrap(false);
            memoFrame.textArea.setWrapStyleWord(false);
            memoFrame.iWrap.setText("Word Wrap: Off");
        }
    }

    public void createFont(int size) {
        arial = new Font("Arial", Font.PLAIN, size);
        csms = new Font("Comic Sans MS", Font.PLAIN, size);
        tnr = new Font("Times New Roman", Font.PLAIN, size);

        setFont(selectedFont);
    }

    public void setFont(String font) {
        selectedFont = font;
        switch(selectedFont) {
            case "Arial":
                memoFrame.textArea.setFont(arial);
                break;
            case "CSMS":
                memoFrame.textArea.setFont(csms);
                break;
            case "TNR":
                memoFrame.textArea.setFont(tnr);
                break;
        }
    }
}
