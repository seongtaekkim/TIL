package me.staek.memo.item;

import me.staek.memo.menu.AbstractMenu;
import me.staek.memo.MemoFrame;

import java.awt.*;
import java.io.*;

public class FileItem extends AbstractMenu {
    String fileName;
    String path;

    public FileItem(MemoFrame memoFrame) {
        super(memoFrame);
    }

    public void newFile() {
        memoFrame.textArea().setText("");
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
        FileDialog dialog = new FileDialog(memoFrame.frame(), "Open", FileDialog.LOAD);
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            fileName = dialog.getFile();
            path = dialog.getDirectory();
            memoFrame.frame().setTitle(fileName);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path + fileName));
             DataInputStream  info = new DataInputStream(new FileInputStream("cfg/" +fileName + ".cfg"))) {
            memoFrame.textArea().setText("");
            String line = null;
            while ((line = br.readLine()) != null) {
                memoFrame.textArea().append(line + "\n");
            }
            System.out.println(info.readInt());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {

        if (fileName == null) {
            saveAs();
        } else {
            try (FileWriter fw = new FileWriter(path + fileName);
                 DataOutputStream  info = new DataOutputStream(new FileOutputStream("cfg/" +fileName + ".cfg"))) {
                System.out.println(path);
                fw.write(memoFrame.textArea().getText());
                info.writeInt(3333);
//                info.write();
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
