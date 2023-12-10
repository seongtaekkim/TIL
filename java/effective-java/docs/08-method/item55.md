# item55 옵셔널 반환은 신중히 하라

- Optional Return 은 Item 7: Eliminate obsolete object references, item30 이왕이면 제네릭 메서드로 만들라 에서 다룬 바 있다.



Optional 은 원소를 최대 1개 가질 수 있는 불변 컬렉션이다. (Collection<T> 를 확장하지 않았지만 원칙적으로 그렇다고한다.)



### 값을 반환할 수 없을 때 두 가지 선택지 - Java 8 이전 

1. 예외를 던진다. 
   - 문제점: 진짜 예외적인 상황에서만 사용해야 하며 (item69), 예외 생성시점에서 stracktrace 전체를 캡처하기에 비용이 크다. 

2. 반환 타입이 객체일 경우 null을 반환한다.
   - 문제점: 예외 생성으로 인한 비용은 들지 않지만, 호출 클라이언트는 null check 코드가 필수.



### Optional API의 등장 - Java 8 이후

- 해당 객체는 제네릭 타입 매개변수로 선언한 객체를 참조하거나 혹은 비어있는 상태로 존재할 수 있다. 
  - Optional은 하나의 값을 가지는 불변 컬렉션(Immutable Collection)

- Optional<T> 객체가 반환되기에 NPE 문제에서 좀 더 자유로워진다. 또한, 예외를 던지는 비용도 줄일 수 있다. 

~~~java
// item30.RecursiveTypeBound
//before java 8
public static <E extends Comparable<E>> E max(Collection<E> c) {
	if(c.isEmpty()){ 
		throw new IllegalArgumentException();
	}
	E result = null;
	for (E e  C) {
		if(result == null  || e.compareTo(result) > 0){
			result = Objects.requireNonNull(e);
		}
	}

	return result;
}
~~~

~~~java
// item30.RecursiveTypeBound
//after java 8
public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
	if(c.isEmpty()){ 
		return Optional.empty();
	}
	E result = null;
	for (E e  C) {
		if(result == null  || e.compareTo(result) > 0){
			result = Objects.requireNonNull(e);
		}
	}

	return Optional.of(result);
}
~~~

- Optional을 반환하는 메서드는 절대 null을 반환하지 말아야 한다.



~~~java
// stream 으로 max 구현 (비교자 인자 필수)
public static <E extends Comparable<E>>
Optional<E> max(Collection<E> c) {
    return c.stream().max(Comparator.naturalOrder());
}
~~~





### Optional API는 언제 사용해야 할까?

- Optional은 검사 예외와 취지가 비슷하다 (item71)
  - 반환 값 여부를 API 사용자에게 명확히 알려준다. (비검사예외, null 은 인지 못할 수 있음)
  - 검사예외를 던지면 클라이언트는 반드시 대처하는 코드를 작성해야 한다.
  - 메서드가 Optional을 반환하면, 호출자는 값을 못받았을 때 행동을 해야하는데, 그중 하나가 기본 값설정이다.

~~~java
//case 1. 기본값 설정
String lastWordInLexicon = max(words).orElse("단어 없음...");

/**
* case 2. 예외 던지기
* - 팩터리를 인자로 넣었따.
* - 예외가 실제 발생하지 않는 한 예외생성 비용은 없다.
*/
Toy myToy = max(toys).orElseThrow(TemperTantrumException::new);
~~~





### 주의사항

1. 반환값으로 Optional을 사용하는것이 항상 정답은 아니다.
   - Collection, Stream, Array, Optional같은 컨테이너 타입을 Optional로 감싸서는 안된다. 
   - 다시 말해, 비어있는 Optional<List<T>>가 아니라 비어있는 List<T>를 반환하는게 좋다는 의미이다.  빈 컨테이너를 그대로 반환하면 클라이언트는 Optional 처리코드를 굳이 작성하지 않아도 된다.

2. 박싱된 기본 타입을 담은 Optional을 반환하지 않도록 하자. 
   - 기본적으로 박싱된 타입을 담는 Optional API는 Primitive Type 값에 대해서도 박싱을 해서 담기 때문에 더 무거울 수 밖에 없다. 그래서 Stream API이나 표준 함수형 인터페이스에서 기본 타입들을 지원하는 전용 클래스(ex:* *IntStream**,* *LongStream**,* *IntFunction**,* *IntConsumer**, ...)를 제공하는 것처럼* *OptionalInt**,* *OptionalLong**,* *OptionDouble* *클래스를 제공한다.  그러니 박싱된 기본 타입을 담는 Optional을 사용하는일은 없도록 해야 한다.



### 객체에 필드로 Optional<T> 타입을 저장할 일이 있을까? 

- 일반적으로는 사용하지 않는게 좋지만, 때로는 적절한 상황도 있을 수 있다. 
- 아래 item02 builter pattern의 영양정보 클래스에 필수,선택 필드 모두 기본타입이다.
  선택필드라는 표현을 위해 필드를 Optional 로 나타내기에 적합한 상황이다.하지만, 모두 기본 타입이기에 옵셔널하다는 정보를 나타내기도 애매한데, 이런 경우 필드 자체를 Optional로 선언할 수도 있을 것이다. 

~~~java
// item55.NutritionFacts
public class NutritionFacts {
    private final int calories;
    private final OptionalInt sodium;
  
    public OptionalInt getCalories() {
        return OptionalInt.of(calories);
    }

    public OptionalInt getSodium() {
        return sodium;
    }  
  ...
}


public static void main(String[] args) {
    NutritionFacts cocaCola = new Builder(240, 8)
            .calories(100)
            .sodium(35)
            .carbohydrate(27).build();

    System.out.println(cocaCola.getCalories().getAsInt());
    System.out.println(cocaCola.getSodium().isPresent() ? cocaCola.getSodium().getAsInt() : "not data");
}
~~~





## 정리

값을 반환하지 못할 가능성이 있고, 호출할 때마다 반환값이 없을 가능성을 염두에 둬야 하는 메서드라면 옵션ㄹ을 반환해야 할 상황일 수 있다.

하지만 옵셔널 반환에는 성능이슈가 있으니, 민감한 경우에 null을 반환하거나 예외를 던지는게 나을 수 있다.

옵셔널을 반환값 이외의 용도로 쓰이는 경우는 매우 드물다.