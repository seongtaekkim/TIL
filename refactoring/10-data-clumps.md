## 냄새 10. 데이터 뭉치 (Data Clumps)

- 항상 뭉쳐 다이는 데이터는 한 곳으로 모아두는 것이 좋다.
  - 여러 클래스에 존재하는 비슷한 필드 목록
  - 여러 함수에 전달하는 매개변수 목록
- 관련 리팩토링 기술
  - “클래스 추출하기 (Extract Class)”를 사용해 여러 필드를 하나의 객체나 클래스로 모을 수있다.
  - “매개변수 객체 만들기 (Introduce Parameter Object)” 또는 “객체 통째로 넘기기 (Preserve Whole Object)”를 사용해 메소드 매개변수를 개선할 수 있다.



1. Employee, Office class 에서 사용하는 AreaCode, Number는 이름만 다를 뿐 사실상 역할이 같고 항상 같이 사용되는 맴버변수들이다. 이러한 코드를 Data Clumps 라고 하고, 이를 하나의 클래스로 모아서 변수 명을 통일하고 로직을 단순화 할 수 있다.

```java
public class Employee {

    private String name;

    private String personalAreaCode;

    private String personalNumber;

    public Employee(String name, String personalAreaCode, String personalNumber) {
        this.name = name;
        this.personalAreaCode = personalAreaCode;
        this.personalNumber = personalNumber;
    }

    public String personalPhoneNumber() {
        return personalAreaCode + "-" + personalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonalAreaCode() {
        return personalAreaCode;
    }

    public void setPersonalAreaCode(String personalAreaCode) {
        this.personalAreaCode = personalAreaCode;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }
}
public class Office {

    private String location;

    private String officeAreCode;

    private String officeNumber;

    public Office(String location, String officeAreCode, String officeNumber) {
        this.location = location;
        this.officeAreCode = officeAreCode;
        this.officeNumber = officeNumber;
    }

    public String officePhoneNumber() {
        return officeAreCode + "-" + officeNumber;
    }

    public String getOfficeAreCode() {
        return officeAreCode;
    }

    public void setOfficeAreCode(String officeAreCode) {
        this.officeAreCode = officeAreCode;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
```



1. TelephoneNumber에 데이터뭉치인 areaCode, number를 맴버변수로 만든다.
2. 이를 사용하는 class는 생성자로 TelephoneNumber를 받고, 변수를 사용할 때에도 TelephoneNumber객체를 참조할 수 있도록 변경한다.

```java
public class TelephoneNumber {
    private String areaCode;

    private String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return this.areaCode + "-" + this.number;
    }
}
public class Office {

    private String location;

    private TelephoneNumber telephoneNumber;

    public Office(String location, TelephoneNumber telephoneNumber) {
        this.location = location;
        this.telephoneNumber = telephoneNumber;
    }

    public String officePhoneNumber() {
        return this.telephoneNumber.toString();
    }

    public String getOfficeAreCode() {
        return this.telephoneNumber.officeAreaCode();
    }

    public void setOfficeAreCode(String officeAreCode) {
        this.telephoneNumber.setOfficeNumber(officeAreCode);
    }

    public String getOfficeNumber() {
        return this.telephoneNumber.officeNumber();
    }

    public void setOfficeNumber(String officeNumber) {
        this.telephoneNumber.setOfficeNumber(officeNumber);
    }
}
public class Employee {

    private String name;

    private TelephoneNumber telephoneNumber;

    public Employee(String name, TelephoneNumber telephoneNumber) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
    }

    public String personalPhoneNumber() {
        return this.telephoneNumber.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TelephoneNumber getTelephoneNumber() {
        return telephoneNumber;
    }
}
```