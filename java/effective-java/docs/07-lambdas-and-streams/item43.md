# item43 람다보다는 메서드 참조를 사용하라



## 메서드참조 종류

| 메서드 참조유형    | 예시                    | 같은 기능을 하는 람다                                     |
| ------------------ | ----------------------- | --------------------------------------------------------- |
| 정적               | Integer::parseInt       | str -> Integer.parseInt(str)                              |
| 한정적(인스턴스)   | Instanse.now()::isAfter | Instance tehn = Instance.now();<br />t -> then.isAfter(t) |
| 비한정적(인스턴스) | String::toLowerCase     | str -> str.toLowerCase()                                  |
| 클래스 생성자      | TreeMap<K,V>::new       | () -> new TreeMap<K, V>()                                 |
| 배열 생성자        | int[]::new              | len -> new int[len]                                       |



**생성자 참조는 팩터리 객체로 사용된다.**

- default 생성자는 Supplier 타입을 가질 수 있음

~~~java
Supplier<_02_Greeting> newGreeting = _02_Greeting::new;
~~~



**비한정적 참조는 주로 스트림 파이프라인에서 매핑과 필터함수에 쓰인다 (item45)**

~~~
~~~





## 유용한 케이스

### 간결함

메서드 참조 > 람다 > 익명 클래스

~~~java
Map<String, Integer> frequencyTable = new TreeMap<>();
String[] arr = {"10", "100"};
for (String s : arr)
    frequencyTable.merge(s, 1, (a, b) -> Integer.sum(a, b)); // Lambda
System.out.println(frequencyTable);
~~~

~~~java
for (String s : arr)
    frequencyTable.merge(s, 1, Integer::sum); // Method reference
System.out.println(frequencyTable);
~~~

~~~java
public final class Integer {
	public static int sum(int a, int b) {
  	return a + b;
	}
...
~~~



**메서드 레퍼런스보다 람다가 간결한 경우**

- 람다를쓰자

~~~java
service.execute("매우긴클래스이름"::"method");

service.execute(() -> action());
~~~



## 단점

### 람다는 안되고 메서드참조는 가능한 경우

https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.9

~~~java
class MyInfo {
    static String myNameis() {
        return "staek";
    }
}

interface G1 {  <E extends Exception> Object m() throws E; }
interface G2 {  <F extends Exception> String m() throws Exception; }
interface G extends G1, G2 {}


/**
 * https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.9
 * Example 9.9-2. Generic Function Types
 *
 * 메서드레퍼런스에는 동작하고 람다는 동작하는 예제 작성.
 * - 인터페이스에 선언된 제네릭 메서드에 경우, 람다에서는 제네릭 메서드타입을 선언하는 문법이 없어 동작하지 않는다.
 */
public class _03_MethodReferenceWithGenericMethod {
    static String check(G g) throws Exception {
        return g.m();
    }

    public static void main(String args[]) throws Exception {
//        그니까 ... myNameis 말고도 Object 반환 타입하는 customMethod라는게 있으면...
//
//        당연히 되겠지 해보고 와야게따
//        String name = check(() -> MyInfo.myNameis()); //
        Supplier<String> myNameis = MyInfo::myNameis;
        String name = check(MyInfo::myNameis);
        System.out.println(name);
    }
}
~~~







## 정리

메서드 참조는 람다의 간단명료한 대안이 될 수 있다.
**메서드 참조 쪽이 짧고 명확하다면 메서드 참조를 쓰고, 그렇지 않을때만 람다를 사용하라.**





