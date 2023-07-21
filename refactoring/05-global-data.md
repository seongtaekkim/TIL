# **냄새5. 전역데이터 (Global Data)**

- 전역 데이터 (예, 자바의 public static 변수)
- 전역 데이터는 아무곳에서나 변경될 수 있다는 문제가 있다.
- 어떤 코드로 인해 값이 바뀐 것인지 파악하기 어렵다.
- 클래스 변수 (필드)도 비슷한 문제를 겪을 수 있다.
- “변수 캡슐화하기 (Encapsulate Variable)”를 적용해서 접근을 제어하거나 어디서 사용하는지 파악하기 쉽게 만들 수 있다.
- 파라켈수스의 격언, “약과 독의 차이를 결정하는 것은 사용량일 뿐이다.”



## **리팩토링 17. 변수 캡슐화하기 (Encapsulate Variable)**

- 메소드는 점진적으로 새로운 메소드로 변경할 수 있으나, 데이터는 한번에 모두 변경해야 한다.
- 데이터 구조를 변경하는 작업을 그보다는 조금 더 수월한 메소드 구조 변경 작업으로 대체할 수 있다.
- 데이터가 사용되는 범위가 클수록 캡슐화를 하는 것이 더 중요해진다.
  - 함수를 사용해서 값을 변경하면 보다 쉽게 검증로직을 추가하거나 변경에 따르는 후속작업을 추가하는 것 이 편리하다.
- 불변 데이터의 경우에는 이런 리팩토링을 적용할 필요가 없다.

1. public static 변수는, 아무 제약없이 값이 변경될 수 있다.
2. 따라서 모두 private 변경 후, method로 감싸준다.
3. 값을 set할 때에는, 적당한 validate를 추가한다.
4. 전역변수는.. public static final 과 같이 readonly 가 아니라면 사용될 일이거의 없는것이 맞는 것일 것이다..

```java
public static void main(String[] args) {
        System.out.println(Thermostats.targetTemperature);
        Thermostats.targetTemperature = 68;
        Thermostats.readInFahrenheit = false;
}
public static Integer targetTemperature = 70;

  public static Boolean heating = true;

  public static Boolean cooling = false;

  public static Boolean readInFahrenheit = true;
public static void main(String[] args) {
        System.out.println(Thermostats.getTargetTemperature());
        Thermostats.setTargetTemperature(68);
        Thermostats.setReadInFahrenheit(false);
}
private static Integer targetTemperature = 70;

  private static Boolean heating = true;

  private static Boolean cooling = false;

  private static Boolean readInFahrenheit = true;

  public static Integer getTargetTemperature() {
      return targetTemperature;
  }

  public static void setTargetTemperature(Integer targetTemperature) {
      // TODO add validate
      Thermostats.targetTemperature = targetTemperature;
      // TODO set notify
  }
..
```