package me.staek.chapter06.item37;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

class Person {

    private String Country; // 나라
    private String City; // 도시
    private String name; // 이름

    public Person(String country, String city, String name) {
        Country = country;
        City = city;
        this.name = name;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "Country='" + Country + '\'' +
                ", City='" + City + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

public class GroupbyEx {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("한국", "서울", "kim"));
        list.add(new Person("한국", "서울", "kim"));
        list.add(new Person("중국", "서울", "kim"));
        list.add(new Person("일본", "서울", "kim"));
        Map<String, List<Person>> collect = list.stream().collect(groupingBy(new Function<Person, String>() {
            @Override
            public String apply(Person person) {
                return person.getCountry();
            }
        }));
        collect.keySet().stream().forEach(System.out::println);
        System.out.println("=======================");
        Map<String, Map<String, List<Person>>> collect1
                = list.stream().collect(groupingBy(Person::getCountry, groupingBy(Person::getCity)));
        System.out.println(collect1.get("한국").get("서울"));
        collect1.keySet().forEach(System.out::println);;
    }
}
