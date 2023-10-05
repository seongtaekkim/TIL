package me.staek.memo;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class FileMenu {
    MemoFrame memoFrame;
    String fileName;
    String path;
    public FileMenu(MemoFrame memoFrame) {
        this.memoFrame = memoFrame;
    }
    public void newFile() {
        memoFrame.textArea.setText("");
        memoFrame.frame.setTitle("New");
        fileName = null;
        path = null;
    }

    public void open() {
        FileDialog dialog = new FileDialog(memoFrame.frame, "Open", FileDialog.LOAD);
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            fileName = dialog.getFile();
            path = dialog.getDirectory();
            memoFrame.frame.setTitle(fileName);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + fileName));
            memoFrame.textArea.setText("");
            String line = null;
            while ((line = br.readLine()) != null) {
                memoFrame.textArea.append(line + "\n");
            }
            br.close();
        } catch(Exception e) {
            System.out.println("noe opened");
        }
    }

    public void save() {

        if (fileName == null) {
            saveAs();
        } else {
            try {
                FileWriter fw = new FileWriter(path + fileName);
                fw.write(memoFrame.textArea.getText());
                fw.close();
            } catch(Exception e) {
                System.out.println("wrong");
            }
        }
    }

    public void saveAs() {
        FileDialog dialog = new FileDialog(memoFrame.frame, "Save", FileDialog.SAVE);
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            fileName = dialog.getFile();
            path = dialog.getDirectory();
            memoFrame.frame.setTitle(fileName);
        }

        try {
            FileWriter fw = new FileWriter(path + fileName);
            fw.write(memoFrame.textArea.getText());
            fw.close();
        } catch(Exception e) {
            System.out.println("wrong");
        }
    }

    public void exit() {
        System.exit(0);
    }
}
