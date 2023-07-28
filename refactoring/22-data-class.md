# 냄새 22. 데이터 클래스 (Data Class)

- 데이터 클래스: public 필드 또는 필드에 대한 게터와 세터만 있는 클래스.
  - 코드가 적절한 위치에 있지 않기 때문에 이러한 냄새가 생길 수 있다.
  - 예외적으로 “단계쪼개기”에서 중간데이터를 표현하는데 사용할 레코드는 불변객체로 데이터를 전달하는 용도로 사용할 수 있다.
- public 필드를 가지고 있다면 **“레코드 캡슐화하기 (Encapsulate Record)”**를 사용해 게터나 세터를 통해서 접근하도록 고칠 수 있다.
- 변경되지 않아야 할 필드에는“세터 제거하기 (Remove Setting Method)”를 적용할 수 있다.
- 게터와 세터가 사용되는 메소드를 찾아보고 “함수 옮기기 (Move Function)”을 사용해서 데이터 클래스로 옮길 수 있다.
- 메소드 전체가 아니라 일부 코드만 옮겨야 한다면 “함수 추출하기 (Extract Function)”을 선행한 뒤에 옮길 수 있다.



## 리팩토링 42. 레코드 캡슐화하기 (Encapsulate Record)

- 변하는 데이터를 다룰 때는 레코드 보다는 객체를 선호한다.
  - 여기서“레코드”란, public 필드로 구성된 데이터 클래스를 말함.
  - 데이터를 메소드 뒤로 감추면 객체의 클라이언트는 어떤 데이터가 저장되어 있는지 신 경쓸 필요가 없다.
  - 필드 이름을 변경할 때 점진적으로 변경할 수 있다.
  - 하지만 자바의 Record는 불변 객체라서 이런 리팩토링이 필요없다.
- public 필드를 사용하는 코드를 private 필드와 게터, 세터를 사용하도록 변경한다.

### 변경 전

- 맴버변수를 public으로 정의해서, 외부에서 필드를 접근하고 변경할 수 있다,.

```java
public class Organization {

    public String name;

    public String country;

}
```

### 변경 후

- java17부터 사용가능한 record로 정의하면, 기본적으로 getter가 만들어져 있고, 불변객체이기 때문에 수정할 수 없게 되어있다.

```java
public record Organization(String name, String country) {

}
class OrganizationTest {

    @Test
    void test() {
        Organization o = new Organization("kim", "korea");
        o.name();
        o.country();
    }

}
```