

## Method Reference

- https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html



- 익명클래스, 람다표현식의 문법을 메소드레퍼런스로 표현할 수 있다.

~~~java
public class Person {

    LocalDate birthday;

    public Person() {
        System.out.println("default constructor");
    }

    public Person(LocalDate birthday) {
        this.birthday = birthday;
    }

    public static int compareByAge3(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }
  
    public int compareByAge(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }

    public int compareByAge2(Person b) {
        return this.birthday.compareTo(b.birthday);
    }
   ...
~~~



~~~java
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person(LocalDate.of(1982, 7, 15)));
        people.add(new Person(LocalDate.of(2011, 3, 2)));
        people.add(new Person(LocalDate.of(2013, 1, 28)));

        people.sort(new Comparator<Person>() {
            @Override
            public int compare(Person a, Person b) {
                return a.birthday.compareTo(b.birthday);
            }
        });
       
    }
~~~

- sort 함수에 맞는 `sort(Comparator<? super E> c) ` 인자를 전달하기 위해 익명클래스로 compare 를 구현한 객체를 넘겨줄 수 있다.

- 이는 람다표현식으로 바꾸어 표현할 수 있다.

  ~~~
  people.sort((a, b) -> a.birthday.compareTo(b.birthday));
  ~~~

~~~java
@FunctionalInterface
public interface Comparator<T> {
    int compare(T o1, T o2);
    ...
}
~~~





### 스태틱 메소드 레퍼런스

~~~java
public class Person{
    public static int compareByAge3(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }
  ...
}

public static void main(String[] args) {
        people.sort(Person::compareByAge3);
}
~~~

- class name 으로 메서드레퍼런스를 접근해서 인자로 넘길 수 있다.



### 인스턴스 메소드 레퍼런스

~~~java
public class Person{
    public int compareByAge(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }
  ...
}

public static void main(String[] args) {
        Person p = new Person();
        people.sort(p::compareByAge);
}
~~~

- static method 가 아닌경우, class name으로 참조할 수 없고, 인스턴스로 참조할 수 있다.



### 임의 객체의 인스턴스 메소드 레퍼런스

~~~java
public class Person {  
public int compareByAge2(Person b) {
        return this.birthday.compareTo(b.birthday);
    }
..
}
  
public static void main(String[] args) {
	people.sort(Person::compareByAge2);
}
~~~

- comparable 의 첫번째 인자는 자기자신, 두번째 인자가 첫번째 인자를 받도록 compareByAge2 함수를 만들면
  임의객체의 인스턴스 메소드 레퍼런스를 사용할 수 있다.



### 생성자 레퍼런스

~~~java
public static void main(String[] args) {

    List<LocalDate> dates = new ArrayList<>();
    dates.add(LocalDate.of(1982, 7, 15));
    dates.add(LocalDate.of(2011, 3, 2));
    dates.add(LocalDate.of(2013, 1, 28));
  //List<Person> collect = dates.stream().map(d -> new Person(d)).collect(Collectors.toList());
    List<Person> collect = dates.stream().map(Person::new).collect(Collectors.toList());
    
  	Function<LocalDate, Person> function = Person::new;
  	Supplier<Person> function1 = Person::new;

}

~~~

- 기본생성자 : input 없고 return 있는 Supplier 를 리턴받도록 생성 가능.
- 인자1개 생성자 : input 1개, return 1개 있는 Function 타입으로 생성 가능.

















​	