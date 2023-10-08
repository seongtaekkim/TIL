package me.staek.memo.view.decorator;

import me.staek.memo.Format;
import me.staek.memo.WordWrap;

import javax.swing.*;
import java.util.Optional;

public class WordWrapDecorator extends Decorator {
    public WordWrapDecorator(Component component) {
        super(component);
    }

    @Override
    public JTextArea textAreaDecorate(JTextArea jTextArea, Optional<Format> format) {
        format.ifPresentOrElse((f) -> {
            jTextArea.setLineWrap(format.get().getWrap().getOnoffWrap());
            jTextArea.setWrapStyleWord(format.get().getWrap().getOnoffWrap());
        }, () ->  {
            jTextArea.setLineWrap(WordWrap.WORD_WRAP_INIT.getOnoffWrap());
            jTextArea.setWrapStyleWord(WordWrap.WORD_WRAP_INIT.getOnoffWrap());
        });

        return super.textAreaDecorate(jTextArea, format);
    }
}
