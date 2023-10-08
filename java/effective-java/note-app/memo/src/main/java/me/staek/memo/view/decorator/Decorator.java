package me.staek.memo.view.decorator;

import me.staek.memo.Format;

import javax.swing.*;
import java.util.Optional;

public abstract class Decorator implements Component {
    Component component;
    public Decorator(Component component) {
        this.component =component;
    }

    @Override
    public JTextArea textAreaDecorate(JTextArea textArea, Optional<Format> format) {
        return component.textAreaDecorate(textArea, format);
    }
}
