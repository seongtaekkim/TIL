package me.staek.memo.item;

import me.staek.memo.WordWrap;
import me.staek.memo.fao.FormatFAO;
import me.staek.memo.menu.AbstractMenu;
import me.staek.memo.MemoFrame;

public class WordWrapItem extends AbstractMenu {
    public WordWrapItem(MemoFrame memoFrame) {
        super(memoFrame);
    }

    public void wordWarp() {
        WordWrap wrap = FormatFAO.getFormat().get().getWrap();

        if (wrap.getOnoffWrap() == false) {
            wrap.setOnoffWrap(true);
            FormatFAO.edit(wrap);
            memoFrame.textArea().setLineWrap(wrap.getOnoffWrap());
            memoFrame.textArea().setWrapStyleWord(wrap.getOnoffWrap());
        } else if (wrap.getOnoffWrap() == true) {
            wrap.setOnoffWrap(false);
            FormatFAO.edit(wrap);
            memoFrame.textArea().setLineWrap(wrap.getOnoffWrap());
            memoFrame.textArea().setWrapStyleWord(wrap.getOnoffWrap());
        }

    }

    @Override
    public void doItem(String command) {
        wordWarp();
    }
}
