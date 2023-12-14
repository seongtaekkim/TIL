# item58 전통적인 for 문 보다는 for-each문을 사용하라



for-each : enhanced for statement



### for와 for-each

- for 문으로 컬렉션을 순회하는 코드

~~~java
for (Iterator<Element> i = c.iterator() ; i.hasNext() ; ) {
	Element e = i.next();
	...
}
~~~

- for 문으로 배열을 순회하는 코드

~~~java
for (int i=0 ; i<a.length ; i++) {
...
}
~~~



반복자와 인섹스 변수를 사용하지 않으니 코드가 깔끔해지고 오류나지 않는다.

하나의 관용구로 컬렉션과 배열 모두 처리할 수 있어서 어떤 컨테이너를 다루는 지 신경 안써도 된다.

~~~java
for (Element e : element) {
...
}
~~~



### 중첩순회 예시

- for문을 생각없이 쓰면 오류가 날 수 있다.
  - 아래는 내부반복에서 외부 반복자의 next()를 계속 호출하여 NoSuchElementException이 발생하였다.
  - 운이 나쁘면 에러조차 나지 않는 상황이 발생할 수 있다.

~~~java
enum Suit { CLUB, DIAMOND, HEART, SPADE }
enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
    NINE, TEN, JACK, QUEEN, KING }

static Collection<Suit> suits = Arrays.asList(Suit.values());
static Collection<Rank> ranks = Arrays.asList(Rank.values());


public static void main(String[] args) {
	List<Card> deck = new ArrayList<>();

	for (Iterator<Suit> i = suits.iterator(); i.hasNext(); )
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
        deck.add(new Card(i.next(), j.next()));
}
~~~

- for-each는 훨씬 간단하고 오류가능성 없이 코드를 작성할 수 있다.

~~~java
for (Suit suit : suits)
    for (Rank rank : ranks)
        deck.add(new Card(suit, rank));
~~~



### for-each 사용 못하는 상황

**파괴적인 필터링** 

- 컬렉션을 순회하면서 선택된 원소를 제거해야 한다면 반복자의 remove를 호출해야 한다.
- java8부터는 Collection의 removeIf 메서드를 사용해 컬렉션을 명시적으로 순회안해도 된다.

**변형 (transforming)**

- 리스트나 배열을 순회하면서 그 원소의 값 일부 혹은 전체를 교체해야 한다면 리스트의 반복자나 배열의 인덱스를 ㅏㅅ용해야 한다.

**병렬반복(parallel iteration)**

- 여러 컬렉션을 병렬로 순회해야 한다면 각각의 반복자와 인덱스 변수를 사용해
  엄격하고 명시적으로 제어해야 한다 (ex 코드58-4)



for-each 문은 컬렉션과 배열은 물론 Iterator 인터페이스를 구현한 객체라면 무엇이든 순회할 수 있다.

~~~java
public interface Iterable<T> {
    Iterator<T> iterator();
}
~~~



### Iterable 구현

원소들의 묶음을 표현하는 타입을 작성해야 한다면 Iterable를 구현해 보자.

Iterator를 구현해 두면 그 타입을 사용하는 프로그래머가 for-each문을 사용할 때마다 여러분에게 감사할 것이다.



## 정리

for 에 비해 for-each는 명료하고 유연하고 버그를 예방한다. 성능저하도 없다.
가능한 for-each를 쓰자.







Iterator 구현 참조

https://rst0070.github.io/java/java-Iterator.html

https://www.youtube.com/watch?v=mzpgeRuYduY

https://www.youtube.com/watch?v=arkoC146TfQ

https://komas.tistory.com/40



