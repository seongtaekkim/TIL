package me.staek.chapter09.item58.custom_iterable;

public class Main {
    public static void main(String[] args) {
        EffectiveClass<Student> myClass  = new EffectiveClass<>();

        myClass.add(new Student("seongtki", "java"));
        myClass.add(new Student("samin", "spring"));
        myClass.add(new Student("jseo", "armeria"));
        myClass.add(new Student("hannkim", "GG"));
        myClass.add(new Student("san", "Crawling"));

        for (Student s : myClass) {
            System.out.println(s.getName());
        }
    }
}
