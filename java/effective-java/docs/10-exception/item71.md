# item71 필요없는 검사예외 사용은 피하라



### 개요

- Checked Exception은 예외가 발생하면 이 문제를 개발자가 처리해 안전성을 높이게 해준다 (코드가 다소 복잡해 지더라도.)

~~~java
public static void main(String[] args) {
    occurrenceCheckedException(Boolean.parseBoolean(args[0]));
}

private static void occurrenceCheckedException(boolean flag) throws IOException {
    if (flag) {
        throw new IOException("임의의 예외 발생");
    }
}
~~~



- 예외를 던지는 메서드는 스트림 내부에서는 직접적으로 사용할 수 없으니 주의.

~~~java
Stream.of(List.of(new Integer[]{1, 2, 3, 4}))
        .filter(f -> {
               throw new IOException(); // compile error
	             return f.size() == 1;
        });
~~~



###  의미 있는 처리를 할 수 없다면 Unchecked Exception을 사용해라. 

- API를 제대로 사용해도 발생할 예외나, 개발자가 복구할 경우가 아니면 UnChecked Exception 을 사용하자.



### 하나의 검사 예외만 던질 경우 (개발자에게 부담?)

- 이미 catch 문이 있는경우, 예외가 늘어난다고 로직 구조가 변경되지 않지만, 예외가 처음 발생하는 거라면 예외처리 프로세스 고민을 해야 할 것이다.

~~~java
public static void main(String[] args) {
    try {
        occurrenceCheckedException(args);
    } catch (IOException | ClassNotFoundException e) {
        //logic...
    }
}

private static void occurrenceCheckedException(String[] args) throws IOException, ClassNotFoundException {
    if (args[0].equals("on")) {
        throw new IOException("임의의 예외 발생");
    }
    if (args[1].equals("on")) {
        throw new ClassNotFoundException("클래스 로드 불가 예외");
    }
}
~~~



### 예외체크 회피 방법

1. Optional 로 리턴받아 로직 처리

2. 메서드를 2개로 쪼개 Unchecked Exception으로 변경
   - 주의사항: item69 상태검사메서드 단점 참고

~~~java
//before 
try {
	obj.action(args);
} catch (CheckedException e) {
	//logics...
}

//after
if(obj.actionPermitted(args)) {
	obj.action(args);
} else {
	//logics...
}
~~~





### 정리

- 꼭 필요한 곳에만 사용한다면 checked exception은 안정성을 높여주지만, 남용하면 쓰기 힘든 API가 된다.
- API호출자가 예외 복구할 수 없다면 unchecked exception 를 던지자.
- **호출자가 복구가능하면** Optional return 을 고려하자.
- 호출자가 Optional 으로 처리하기에 정보가 부족하면 **checked exception을 던지자.**