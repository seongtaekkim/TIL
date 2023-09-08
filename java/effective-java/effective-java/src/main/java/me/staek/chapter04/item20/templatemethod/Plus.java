package me.staek.chapter04.item20.templatemethod;

public class Plus extends FileProcessor {
    public Plus(String path) {
        super(path);
    }

    @Override
    protected int getResult(int result, int number) {
        return result + number;
    }

}
