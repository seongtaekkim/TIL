package me.staek.memo;
import me.staek.memo.code.Menu;
import me.staek.memo.code.Program;

import java.awt.*;

public class FontItem extends FormatMenu {
    public FontItem(MemoFrame memoFrame) {
        super(memoFrame);
        init();
    }

    private void init() {
        memoFrame.textArea()
                .setFont(new Font(Program.DEFAULT_FONT_NAME
                                , Program.DEFAULT_FONT_STYLE
                                , Program.DEFAULT_FONT_SIZE));
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
        this.memoFrame.textArea().setFont(new Font(font.getName(), font.getStyle(), size));
    }

    public void createFont(String name) {
        Font font = this.memoFrame.textArea().getFont();
        this.memoFrame.textArea().setFont(new Font(name, font.getStyle(), font.getSize()));
    }

}
