package me.staek.chapter04.item20.templatemethod;

public class Client {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new Plus("number.txt");
        System.out.println(fileProcessor.process());
        fileProcessor = new Minus("number.txt");
        System.out.println(fileProcessor.process());
    }
}
