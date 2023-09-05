package me.staek.chapter04.item17.immutable_rule;


final class Person {

    private final String name;
    private final Company company;
    public Person(String name, Company company) {
        this.name = name;
        this.company = company;
    }

    public Company getCompany() {
        Company c = new Company();
        c.setName(c.getName());
        c.setLocation(c.getLocation());
        return c;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", company=" + company +
                '}';
    }
}

class Company {
    private String name;
    private String location;


    public String getName() {
        return name;
    }


    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

/**
 * TODO 불변객체규칙 Person class
 *      1. 객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다. - setter 없음
 *      2. 클래스를 확장할 수 없도록 한다 - final class
 *      3. 모든 필드를 final로 선언한다.
 *      4. 모든 필드를 private 으로 선언한다.
 *      5. 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다. - 가변객체 Companay를 접근할 때 복사하여 전달하면, 변경에 안전하다.
 */
public class Rule {
    public static void main(String[] args) {
        Company c = new Company();
        c.setName("naver");
        c.setLocation("seoul");
        Person p = new Person("kim", c);
        System.out.println(p);
        p.getCompany().setName("kakao");

        System.out.println(p);
    }
}
