# item27 비검사 경고를 제거하라

- 제네리글 사용하기 시작하면 수많은 컴파일 경고를 보게 될 것이다.
- 비검사 형변환 경고, 비검사 메서드 호출경고, 비검사 매개변수화 .......
- 이런 경고를 제거하는 방법을 알아보자



### 대부분 비검사 경고는 쉽게 제거할 수 있다.

~~~java
public class XlintTest {

    public static void main(String[] args) {
        Set names = new HashSet();
        names.add("aeg");
        Set<String> strings = new HashSet<>();
    }
}
~~~

- javac -Xlint:unchecked XlintTest.java

~~~java
XlintTest.java:17: warning: [unchecked] unchecked call to add(E) as a member of the raw type Set
        names.add("aeg");
                 ^
  where E is a type-variable:
    E extends Object declared in interface Set
1 warning
~~~



~~~java
Set<String> names = new HashSet<String>();
~~~

- 컴파일러가 알려준대로 하면 경고 사라진다.
- 이런 간단한 경고도 있다. (<> : java7 부터 제공)



### 할수 있는한 모든 비검사 경고를 제거하라

- 모두 제거한다면 그 코드는 타입안정이 보장된다.
- 제거하지 않으면 런타임에 ClassCastException 이 발생할 수 있다.
- 경고를 제거할 수 없지만 타입안전하다고 확신한다면 @SuppressWarnings("unchecked") 를 기술해 경고를 숨기자. (경고는 안뜨는데, 안전하지 않다면 당연히 오류 날 수 있다.)



**경고를 제거할 수 없지만 타입안전하다고 확신한다**
면 @SuppressWarnings("unchecked") 를 기술해서 경고를 숨겨라.

- 만일 타입안전한지 확신할 수 없는데 사용했다면 런타임에 ClassCastException 이 발생할 수 있다.



### @SuppressWarnings 예제

에너테이션은 선언에만 달 수 있기 때문에 return에는 @SuppressWarnings 를 달 수 없다.
-> 지역변수에 임시로 담으면서 기술하라.

~~~java
public <T> T[] toArray(T[] a) {
    if (a.length < size) {
        // 이거 왜 썼냐면 #$%^%$#@
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Arrays.copyOf(elements, size, a.getClass());
        return result;
    }
    System.arraycopy(elements, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;
    return a;
}
~~~

- 이 코드는 컴파일도 되고, 비검가경고를 숨기는 범위도 최소로 좁혔다.
- @SuppressWarnings("unchecked")를 사용할 때면 그 경고를 무시해도 안전한 이유를 주석으로 남겨야 한다.
  다른사람들의 이해에 도움이 되고, 코드 수정 시 사이드이펙트가 있을 수 있기 때문이다.



## 정리

비검사 경고는 중요하지 무시하지 말자.
모든 비검사 경고는 런타임에 ClassCastException을 발생시킬 수 있으니 제거하자.
없앨 방법을 못찾겠다면, 타입안전함을 증명하고 가능한 범위를 좁혀 @SuppressWarnings("unchecked")를 기술하라
그 후 근거로서 주석을 남겨라













