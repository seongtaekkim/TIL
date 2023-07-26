# 냄새 18. 중재자 (Middle Man)

- 캡슐화를 통해 내부의 구체적인 정보를 최대한 감출 수 있다.
- 그러나, 어떤 클래스의 메소드가 대부분 다른 클래스로 메소드 호출을 위임하고 있다면 중재자를 제거하고 클라이언트가 해당 클래스를 직접 사용하도록 코드를 개선할 수 있다.
- 관련 리팩토링
  - **“중재자 제거하기 (Remove Middle Man)”** 리팩토링을 사용해 클라이언트가 필요한 클래스를 직접 사용하 도록 개선할 수 있다.
  - “함수 인라인 (Inlince Function)”을 사용해서 메소드 호출한 쪽으로 코드를 보내서 중재자를 없앨 수도 있다.
  - **“슈퍼클래스를 위임으로 바꾸기 (Replace Superclass with Delegate)”**
  - **“서브클래스를 위임으로 바꾸기 (Replace Subclass with Delegate)**



## 리팩토링 38. 중재자 제거하기 (Remove Middle Man)

- “위임 숨기기”의 반대에 해당하는 리팩토링.
- 필요한 캡슐화의 정도는 시간에 따라 그리고 상황에 따라 바뀔 수 있다
- 캡슐화의 정도를 “중재자 제거하기”와 “위임 숨기기” 리팩토링을 통해 조절할 수 있으다.
- 위임하고 있는 객체를 클라이언트가 사용할 수 있도록 getter를 제공하고, 클라이언트는 메시지 체인을 사용하도록 코드를 고친 뒤에 캡슐화에 사용했던 메소드를 제거한다.
- Law of Demeter를 지나치게 따르기 보다는 상황에 맞게 활용하도록 하자.
  - 디미터의 법칙, “가장 가까운 객체만 사용한다.”

### 변경 전

상황 : keesun.getManager() 를 호출하면 Person가 Department의 데이터를 가져오는, 위임되어 있는상황이다.

이런 와중에 Department 의 변수,크기가 커지게 된다면, Person이 위임역할을 하는게 맞는 것인지 검토를 해야 하고, 위임된 코드를 제거할 수 있도록 리팩토링 해야 한다.

```java
class PersonTest {

    @Test
    void getManager() {
        Person nick = new Person("nick", null);
        Person keesun = new Person("keesun", new Department(nick));
        assertEquals(nick, keesun.getManager());
    }
}
public class Department {

    private Person manager;

    public Department(Person manager) {
        this.manager = manager;
    }

    public Person getManager() {
        return manager;
    }
}
public class Person {

    private Department department;

    private String name;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Person getManager() {
        return this.department.getManager();
    }
}
```

### 변경 후

- Person 에서 Department 를 리턴하게 만들어서, client에서는 department에서 데이터를 가져올수 있게 되었다.

```java
class PersonTest {

    @Test
    void getManager() {
        Person nick = new Person("nick", null);
        Person keesun = new Person("keesun", new Department(nick));
        assertEquals(nick, keesun.getDepartment().getManager());
    }

}
public class Person {

    private Department department;

    private String name;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Person getManager() {
        return this.department.getManager();
    }

    public Department getDepartment() {
        return department;
    }
}
```





## 리팩토링 39. 슈퍼클래스를 위임으로 바꾸기

## (Replace Superclass with Delegate)

- 객체지향에서 “상속”은 기존의 기능을 재사용하는 쉬우면서 강력한 방법이지만 때로는 적절하지 않은 경우도 있다.
- 서브클래스는 슈퍼클래스의 모든 기능을 지원해야 한다.
  - Stack이라는 자료구조를 만들 때 List를 상속 받는것이 좋을까?
- 서브클래스는 슈퍼클래스 자리를 대체하더라도 잘 동작해야 한다.
  - 리스코프 치환 원칙
- 서브클래스는 슈퍼클래스의 변경에 취약하다.
- 그렇다면 상속을 사용하지 않는 것이 좋은가?
  - 상속은 적절한 경우에 사용한다면 매우 쉽고 효율적인 방법이다.
  - 따라서, 우선 상속을 적용한 이후에, 적절치 않다고 판단이 된다면 그때에 이 리팩토링을 적용하자.

### 변경 전

- 상속관계에 있는 구조에서, 자식클래스가 더이상 필요하지 않는 경우 위임기능으로 두 관계를 분리하도록 리팩토링 할 수 있다.

```java
public class Scroll extends CategoryItem {

    private LocalDate dateLastCleaned;

    public Scroll(Integer id, String title, List<String> tags, LocalDate dateLastCleaned) {
        super(id, title, tags);
        this.dateLastCleaned = dateLastCleaned;
    }

    public long daysSinceLastCleaning(LocalDate targetDate) {
        return this.dateLastCleaned.until(targetDate, ChronoUnit.DAYS);
    }
}
public class CategoryItem {

    private Integer id;

    private String title;

    private List<String> tags;

    public CategoryItem(Integer id, String title, List<String> tags) {
        this.id = id;
        this.title = title;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
    }
}
```

### 변경 후

- Scroll class 내부에 부모클래스인 CategoryItem을 맴버변수로 정의하고, 생성자에서 인스턴스를 생성해준다.
  - 원래 상송관계에서 super(..)를 호출했으니, 필요한 인자는 이미 갖고 있다.
- 상속관계를 끊어준다.

```java
public class Scroll {

    private LocalDate dateLastCleaned;

    CategoryItem categoryItem;
    public Scroll(Integer id, String title, List<String> tags, LocalDate dateLastCleaned) {
        this.dateLastCleaned = dateLastCleaned;
        this.categoryItem = new CategoryItem(id, title, tags);
    }

    public long daysSinceLastCleaning(LocalDate targetDate) {
        return this.dateLastCleaned.until(targetDate, ChronoUnit.DAYS);
    }
}
public class Person {

    private Department department;

    private String name;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Person getManager() {
        return this.department.getManager();
    }

    public Department getDepartment() {
        return department;
    }
}
```



## 리팩토링 40. 서브클래스를 위임으로 바꾸기 (Replace Subclass with Delegate)

- 어떤 객체의 행동이 카테고리에 따라 바뀐다면, 보통 상속을 사용해서 일반적인 로직은 슈퍼클래스에 두고 특이한 케이스에 해당하는 로직을 서브클래스를 사용해 표현한다.
- 하지만, 대부분의 프로그래밍 언어에서 상속은 오직 한번만 사용할 수 있다.
  - 만약에 어떤 객체를 두가지 이상의 카테고리로 구분해야 한다면?
  - **위임을 사용하면 얼마든지 여러가지 이유로 여러 다른 객체로 위임을 할 수 있다.**
- 슈퍼클래스가 바뀌면 모든 서브클래스에 영향을 줄 수 있다. 따라서 슈퍼클래스를 변경할 때 서브클래스까지 신경써야 한다.
  - 만약에 서브클래스가 전혀 다른 모듈에 있다면?
  - **위임을 사용한다면 중간에 인터페이스를 만들어 의존성을 줄일 수 있다.**
- “상속 대신 위임을 선호하라.”는 결코 “상속은 나쁘다.”라는 말이 아니다.
  - 처음엔 상속을 적용하고 언제든지 이런 리팩토링을 사용해 위임으로 전환할 수 있다.

다른 클래스를 상속해야

생성자와 달리

1. 팩토리 클래스는 이름을 자유롭게 할수있다.
2. 리턴객체 타입이 좀더 자유롭다.