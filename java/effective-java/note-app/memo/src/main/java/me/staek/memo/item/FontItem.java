package me.staek.memo.item;
import me.staek.memo.dto.MyFont;
import me.staek.memo.fao.FormatFAO;
import me.staek.memo.menu.AbstractMenu;
import me.staek.memo.MemoFrame;
import me.staek.memo.code.Menu;
import me.staek.memo.code.Program;

import java.awt.*;

public class FontItem extends AbstractMenu {
    public FontItem(MemoFrame memoFrame) {
        super(memoFrame);
        init();
    }

    private void init() {
        Font font = MyFont.INIT_FONT;
        FormatFAO.edit(font);
        memoFrame.textArea().setFont(font);
    }

    @Override
    public void doItem(String command) {

        Menu.FONT.children().stream().forEach((f) -> {
            if (f.value().equals(command)) {
                createFont(f.value());
            }
        });
        Menu.FONTSIZE.children().stream().forEach((f) -> {
            if (f.value().equals(command)) {
                createFont(Program.myMap.get(f.value()));
            }
        });
    }

    public void createFont(int size) {
        Font font = this.memoFrame.textArea().getFont();
        Font editFont = new Font(font.getName(), font.getStyle(), size);
        FormatFAO.edit(editFont);
        this.memoFrame.textArea().setFont(editFont);
    }

    public void createFont(String name) {
        Font font = this.memoFrame.textArea().getFont();
        Font editFont = new Font(name, font.getStyle(), font.getSize());
        FormatFAO.edit(editFont);
        this.memoFrame.textArea().setFont(editFont);
    }

}
