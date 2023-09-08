package me.staek.chapter04.item20.templatemethod;

public class Minus extends FileProcessor{
    public Minus(String path) {
        super(path);
    }

    @Override
    protected int getResult(int result, int number) {
        return result - number;
    }
}
