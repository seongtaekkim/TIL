package me.staek.chapter08.item49;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Person implements Comparable<Person>{
    private String name;

    public Person() {}
    public Person(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Person o) {
        return o.name.compareTo(this.name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

}

/**
 * 메서드 유효성검사 - 예외
 * - sort 내부에서 어차피 예외 즉시 런타임에러가 발생하기 때문에
 * - 로직 수행전에 모든 데이터를 검증할 이유가 없다.
 */
public class UnnecessaryCheck {

    public static void sort(List<Person> list) {
        for (Person p : list) {
            if (p.getName() == null)
                throw new NullPointerException();
        }
        Collections.sort(list);
    }

    public static void main(String[] args) {

        List<Person> list = new ArrayList<>();
        list.add(new Person("apple"));
        list.add(new Person("strawberry"));
        list.add(new Person());
        sort(list);
        list.forEach(System.out::println);
    }
}
