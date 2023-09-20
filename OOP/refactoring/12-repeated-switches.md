# 냄새 12. 반복되는 switch 문 (Repeated Swtiches)

- 예전에는 switch 문이 한번만 등장해도 코드 냄새로 생각하고 다형성 적용을 권장했다.
- 하지만 최근에는 다형성이 꽤 널리 사용되고 있으며, 여러 프로그래밍 언어에서 보다 세련된 형태의 switch 문을 지원하고 있다.
- 따라서 오늘날은“반복해서 등장하는 동일한 switch 문”을 냄새로 여기고 있다.
- 반복해서 동일한 switch 문이 존재할 경우, 새로운 조건을 추가하거나 기존의 조건을 변경 할 때모든 switch 문을 찾아서 코드를 고쳐야 할지도 모른다.

### 변경 전

- switch statement 로 작성되었다.
- 아래 소스는 break 구문이 없어 시멘틱 오류가 발생한다,

```java
public class SwitchImprovements {

    public int vacationHours(String type) {
        int result;
        switch (type) {
            case "full-time": result = 120;
            case "part-time": result = 80;
            case "temporal": result = 32;
            default: result = 0;
        }
        return result;
    }
}
```

### 변경 후

- switch expression 으로 작성되었다. (java17)
- 보다 더 깔끔하게 작성된다.

```java
public class SwitchImprovements {

    public int vacationHours(String type) {
        int result = switch (type) {
            case "full-time" -> 120;
            case "part-time" -> 80;
            case "temporal" -> 32;
            default -> 0;
        }
        return result;
    }
}
```