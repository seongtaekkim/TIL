package me.staek.memo.view.decorator;

import me.staek.memo.Format;
import me.staek.memo.MyFont;

import javax.swing.*;
import java.util.Optional;

public class FontDecorator extends Decorator {
    public FontDecorator(Component component) {
        super(component);
    }

    @Override
    public JTextArea textAreaDecorate(JTextArea jTextArea, Optional<Format> format) {
        format.ifPresentOrElse((f) -> jTextArea.setFont(f.getFont()),
                () -> jTextArea.setFont(MyFont.INIT_FONT));
        return super.textAreaDecorate(jTextArea, format);
    }
}
