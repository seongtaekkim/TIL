package me.staek.memo.view.decorator;

import me.staek.memo.Format;

import javax.swing.*;
import java.util.Optional;

public class DefaultComponent implements Component {
    public DefaultComponent() {
    }

    @Override
    public JTextArea textAreaDecorate(JTextArea jTextArea, Optional<Format> format) {
        return jTextArea;
    }
}
