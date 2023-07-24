# 냄새 17. 메시지 체인 (Message Chains)

- 레퍼런스를 따라 계속해서 메소드 호출이 이어지는 코드.
    - 예) this.member.getCredit().getLevel().getDescription()
- 해당 코드의 클라이언트가 코드 체인을 모두 이해해야 한다.
- 체인 중 일부가 변경된다면 클라이언트의 코드도 변경해야 한다.
- 관련 리팩토링
    - **“위임 숨기기 (Hide Delegate)”**를 사용해 메시지 체인을 캡슐화를 할 수 있다.
    - “함수 추출하기 (Extract Function)”로 메시지 체인 일부를 함수로 추출한 뒤, “함수 옮기기
    (Move Function)”으로 해당 함수를 적절한 이동할 수 있다.
    

## 리팩토링 37. 위임 숨기기 (Hide Delegate)

- 캡슐화 (Encapsulation)란 어떤 모듈이 시스템의 다른 모듈을 최소한으로 알아야 한다는 것이
다. 그래야 어떤 모듈을 변경할 때, 최소한의 모듈만 그 변경에 영향을 받을 것이고, 그래야 무언
가를 변경하기 쉽다.
- 처음 객체 지향에서 캡슐화를 배울 때 필드를 메소드로 숨기는 것이라 배우지만, 메소드 호출도
숨길 수 있다.
    - person.department().manager(); -> person.getManager()
    - 이전의 코드는 Department를 통해 Manager에 접근할 수 있다는 정보를 알아야 하지만,
    getManager()를 통해 위임을 숨긴다면 클라이언트는 person의 getManager()만 알아도 된
    다. 나중에 getManager() 내부 구현이 바뀌더라도 getManager()를 사용한 코드는 그대로
    유지할 수 있다
    

### 변경 전

- 아래 테스트 코드에서 keesun.getDepartment().getManager() 와 같이 체이닝 개수가 많을 경우, 위임숨기기를 할 수 있다.

```java
class PersonTest {

    @Test
    void manager() {
        Person keesun = new Person("keesun");
        Person nick = new Person("nick");
        keesun.setDepartment(new Department("m365deploy", nick));

        Person manager = keesun.getDepartment().getManager();
        assertEquals(nick, manager);
    }

}
```

```java
public class Person {

    private String name;

    private Department department;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
```

```java
public class Department {

    private String chargeCode;

    private Person manager;

    public Department(String chargeCode, Person manager) {
        this.chargeCode = chargeCode;
        this.manager = manager;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public Person getManager() {
        return manager;
    }
}
```

### 변경 후

- method extract, method move를 통해 위임숨기기를 하였다
- `keesun.getManager()`

```java
class PersonTest {

    @Test
    void manager() {
        Person keesun = new Person("keesun");
        Person nick = new Person("nick");
        keesun.setDepartment(new Department("m365deploy", nick));

        Person manager = keesun.getManager();
        assertEquals(nick, manager);
    }

}
```

```java
public class Person {

    private String name;

    private Department department;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    Person getManager() {
        return getDepartment().getManager();
    }
}
```