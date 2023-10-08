package me.staek.memo.view.decorator;

import me.staek.memo.Format;

import javax.swing.*;
import java.util.Optional;

public interface Component {

    JTextArea textAreaDecorate(JTextArea jTextArea, Optional<Format> format);
}
