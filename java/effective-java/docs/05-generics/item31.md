# item31 한정적 와일드카드를 사용해 API 유연성을 높이라



### 개요

- 매개변수화 타입은 불공변이다.
- List<String> 은 List<Object> 의 일을 모두 수행 못하니 당연하다 볼수 있따. (item10 리스코프 원칙 위배)
- 불공변 보다 유연한게 필요하다.



아래 예는 매개변수화 타입이 불공변이기 때문에 컴파일에러 발생한다.

~~~java
Stack<Number> numberStack = new Stack<>();
Iterable<Integer> integers = Arrays.asList(3, 1, 4, 1, 5, 9);
numberStack.pushAll(integers);
~~~

~~~java
public void pushAll(Iterable<E> src) {
    for (E e : src)
        push(e);
}
~~~

~~~sh
# 컴파일에러 발생
java: incompatible types: java.lang.Iterable<java.lang.Integer> cannot be converted to java.lang.Iterable<java.lang.Number>
~~~



- 한정적 와일드카드 타입으로 해결할 수 있다. (유연하게 !)

~~~java
public void pushAll(Iterable<? extends E> src) {
    for (E e : src)
        push(e);
}
~~~



## 1. PECS



### 유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드 타입을 사용하라

- 다만 생산자, 소비자 두 역할 모두 한다면 와일드카드를 사용하지 않는다 .지정을 해야 한다.



### PECS: producer-extends, consumer-super

- PECS 공식은 와일드카드 타입을 사용하는 기본 원칙이다.
- 생산자 : <? extends T>
  - T 타입을 리소스에 저장하는 형태.
  - T 타입으로 선언된 자료구조에 저장되야 하니, ? 는 T를 상속하는 형태여야 한다.
- 소비자: <? super T>
  - T 타입을 리소스에서 꺼내는 형태.
  - T 를 꺼내서 외부 리소스에 저장해야 하니, T 이거냐 T가 상속(확장) 한 타입이어야 한다.



### 반환타입에서 와일드카드타입?

- 반환타입은 Set<E> 이다.
  - 반환타입에는 한정적 와일드카드 타입을 사용하면 안된다.
  - 유연성 x, 클라이언트에 와일드카드 타입을 써야 함.
- **만약 클래스 사용자가 와일드카드 타입을 신경써야 한다면, API에 문제가 있을 가능성이 있다.**

~~~java
public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
~~~

- 위 메서 드 사용 시 리턴타입에 대해서는 자바가 추론하여 자동으로 형변환 해준다. (java 8부터 가능)
  - 만약 자바7 이하라면, 명시적 타입 변환을 해주어야 한다.





##  2. Comparator와 Comparable은 소비자

- 아래 예제는 생산자와 소비자가 중첩되게 사용 되었다.
- E는 데이터를 쌓으니 생산자가 되고,
- Comparable는 데이터를 꺼내서 비교하니 소비자가 된다.

~~~java
public static <E extends Comparable<? super E>> E max(List<? extends E> list) {
~~~

- 만약 Comparable<E> 로 정의한다면, 보통은 문제 없지만, 리턴타입이 와일드 카드일 경우 컴파일에러가 발생한다.
  - E의 구체적으로 어떤 상위타입인지 추론할 수 없기 때문.
  - 실제로 부모타입만 Comparable를 구현했을 수 있기 때문에 소비자로서 작성해야 함.

~~~java
List<IntegerBox> list = new ArrayList<>();
list.add(new IntegerBox(10, "effective"));
list.add(new IntegerBox(2, "java"));

System.out.println(max(list));
Box<?> max = max(list); // 컴파일에러
~~~

~~~java
// 컴파일에러
java: incompatible types: inference variable E has incompatible equality constraints 
me.staek.chapter05.item31.pecs.Box<?>,me.staek.chapter05.item31.pecs.Box<java.lang.Integer>
~~~





## 3. 와일드카드 활용

- 메서드 인자의 와일드카드 vs 매개변수화 타입 비교

~~~java
public static void swap(List<?> list, int i, int j) 
public static <E> void swap(List<E> list, int i, int j) 
~~~

### 기본규칙 : 메서드 선언 타입매개변수가 한번만 나오면 와일드카드로 대체하라.

- 비한정적 타입 매개변수라면 비한정적 와일드카드로 바꾸고
- 한정적 타입 매개변수라면 한정적 와일드카드로 바꾼다.



### 문제점

- 컴파일이 안된다.
  - 매개변수로 와일드카드가 입력된 후, 다시 매개변수로 set에 전달한다면
  - **set은 해당 매개변수가 와일드카드로 입력되길 예상하기 때문에 컴파일에러가 발생한다.**

~~~java
public static void swap(List<?> list, int i, int j) {
    list.set(i, list.set(j, list.get(i)));
}
~~~

### 해결방법

- 실제타입으로 변경해주는 메서드를 구현하여 해결한다.

~~~java
public static void swap(List<?> list, int i, int j) {
    swapHelper(list, i, j);
}

// 와일드카드 타입을 실제 타입으로 바꿔주는 private 도우미 메서드
private static <E> void swapHelper(List<E> list, int i, int j) {
    list.set(i, list.set(j, list.get(i)));
}
~~~

### 나라면?

- 이게 뭐가 복잡한가. 그냥 이거 쓰는게 좋다.
- 와일드카드는 PECS 인경우 한정적 와일드타입으로 변경할 때만 사용하고 그 외 단독으로는 사용하지 않는게 더 신경 쓸 일 없고 좋아보임.

```java
public static <E> void swap(List<E> list, int i, int j) {
    list.set(i, list.set(j, list.get(i)));
}
```





## 4. 타입 추론 (Type Inference)

### 타입추론

~~~java
/**
 * 제네릭 메서드 인수에 대한 명시적타입인수
 */
ArrayList<Box<Integer>> listOfIntegerBoxes = new ArrayList<>();
BoxExample.<Integer>addBox(10, listOfIntegerBoxes); // 입력되는 10을 보고 리턴 타입을 추론하는 것임.
BoxExample.addBox(20, listOfIntegerBoxes);
~~~



~~~java
/**
 *  Target Type
 *  메서드 리턴타입이 제네릭인데, 명시적 형변환 없이 타입추론이 가능.
 */
List<String> stringlist = Collections.emptyList();
List<Integer> integerlist = Collections.<Integer>emptyList();
~~~



~~~java
/**
 * Target Type
 * 메서드 인자 타입추론
 */
BoxExample.processStringList(Collections.<String>emptyList());
BoxExample.processStringList(Collections.emptyList());
~~~



### 타입추론의 한계

~~~java
/**
 * TODO 타입추론 한계
 * - comparingInt함수 인자에 명시적형변환을 해야한다. 이후 체이닝 메서드에서는 안해도 된다.
 * - comparingInt함수 인자는 Consumer 여서 정확히 어떤 상위타입인지 추론이 불가능하기에 지정해주어야 하는 듯하다.
 */
private static final Comparator<PhoneNumberComparatorTest> COMPARATOR =
        comparingInt((PhoneNumberComparatorTest pn) -> pn.areaCode)
                .thenComparingInt(pn -> pn.getPrefix())
                .thenComparingInt(pn -> pn.lineNum);
~~~





## 5. 정리

조금 복잡하더라도 와일드카드 타입을 적용하면 API가 훨씬 유연해진다.
그러니 널리 쓰일 라이브러리를 작성한다면 반드시 와일드카드 타입을 적절히 사용해줘야 한다.
PECS공식을 기억하자.
즉, 생사낮(producer)는 extends를 소비자(consumer)는 super를 사용한다.
Comparable과 Comparator는 모두 소비자라는 사실도 잊지 말자.