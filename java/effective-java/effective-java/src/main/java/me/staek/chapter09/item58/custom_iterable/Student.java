package me.staek.chapter09.item58.custom_iterable;

public class Student {
    private final String name;
    private final String major;
    public Student(String name, String major) {
        this.name = name;
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }
}
