package me.staek.memo.item;

import me.staek.memo.MyFont;
import me.staek.memo.fao.FormatFAO;
import me.staek.memo.Format;
import me.staek.memo.view.decorator.Component;
import me.staek.memo.view.decorator.DefaultComponent;
import me.staek.memo.view.decorator.FontDecorator;
import me.staek.memo.view.decorator.WordWrapDecorator;
import me.staek.memo.menu.AbstractMenu;
import me.staek.memo.MemoFrame;

import java.awt.*;
import java.io.*;
import java.util.Optional;

public class FileItem extends AbstractMenu {
    String fileName;
    String path;

    public FileItem(MemoFrame memoFrame) {
        super(memoFrame);
    }

    public void newFile() {
        FormatFAO.createFormat("New");
        FormatFAO.edit(MyFont.INIT_FONT);
        memoFrame.textArea().setText("");
        memoFrame.textArea().setFont(FormatFAO.getFormat().get().getFont());
        memoFrame.frame().setTitle("New");
        fileName = null;
        path = null;
    }

    @Override
    public void doItem(String command) {
        switch(command) {
            case "New": newFile(); break;
            case "Open": open(); break;
            case "Save": save(); break;
            case "SaveAs": saveAs(); break;
            case "Exit": exit(); break;
        }
    }

    public void open() {
        FormatFAO.clear();
        FileDialog dialog = new FileDialog(memoFrame.frame(), "Open", FileDialog.LOAD);
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            fileName = dialog.getFile();
            path = dialog.getDirectory();
            memoFrame.frame().setTitle(fileName);
            try (BufferedReader br = new BufferedReader(new FileReader(path + fileName))) {
                memoFrame.textArea().setText("");
                String line = null;
                while ((line = br.readLine()) != null) {
                    memoFrame.textArea().append(line + "\n");
                }

                Optional<Format> format = FormatFAO.getFormat(fileName);
                Component component = new DefaultComponent();
                component = new FontDecorator(component);
                component = new WordWrapDecorator(component);
                component.textAreaDecorate(memoFrame.textArea(), format);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void save() {

        if (fileName == null) {
            saveAs();
        } else {
            try (FileWriter fw = new FileWriter(path + fileName)) {
                fw.write(memoFrame.textArea().getText());
                FormatFAO.save(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveAs() {
        FileDialog dialog = new FileDialog(memoFrame.frame(), "Save", FileDialog.SAVE);
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            fileName = dialog.getFile();
            path = dialog.getDirectory();
            memoFrame.frame().setTitle(fileName);
            FormatFAO.save(fileName);
        }

        try (FileWriter fw =
                     new FileWriter(path + fileName)) {
            fw.write(memoFrame.textArea().getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit() {
        System.exit(0);
    }
}
