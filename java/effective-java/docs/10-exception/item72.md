# item72 표준예외를 사용하라



###### 숙련된 개발자일수록 많은 코드를 재사용한다.  예외 역시 포함된다. 



### 표준 예외 사용의 장점

- 그럼 내가 별도로 명시적인 예외를 만드는 것과 비교해서 표준 예외 사용이 어떠한 이점을 가져올까?
- 다른 개발자가 익히고 사용하기 쉬워진다. 
- 개발자 입장에선 낯선 프로그램이라고 하더라도 익숙한 표준 예외가 던져진다면, 읽기 쉬울 것이다. 어디서 갑자기 개발자가 임의로 만든 InvalidMoneyAmountByChildException보다는 IllegalArgumentException이 익숙할 것이다.
- 이미 익숙한 규약들을 그대로 따르기에 핸들링하기도 쉬워진다. 
- 예외 클래스 수가 적을수록 메모리 사용량도 줄고 클래스를 적재하는 시간도 적어진다



### Exception, RuntimeException, Throwable, Error는 직접 재사용하지 말자. 

- 상위 클래스 이기 때문에 안정적이지 않을 수 있다. (여러 구현체 성격을 포괄)



### 주 사용되는 표준 예외의 쓰임새

| 예외                            | 주요 쓰임                                                  |
| ------------------------------- | ---------------------------------------------------------- |
| IllegalArgumentException        | 허용하지 않는 값이 인수로 전달되었을 때(Null은 NPE로 처리) |
| IllegalStateException           | 객체가 메서드를 수행하기 적절치 않은 상태일 때             |
| IndexOutOfBoundsException       | 인덱스가 범위를 넘어섰을 때                                |
| ConcurrentModificationException | 허용하지 않는 동시 수정이 발견되었을 때                    |
| UnsupportedOperationException   | 호출한 메서드를 지원하지 않을 때                           |

- 표준예외 보다 더 많으 정보제공을 원한다면 확장해도 된다.
- 예외는 직렬화 할 수 있다는걸 명심하자 (직렬화에 대한 비용 부담)





### 상호 배타적이지 않은 예외

- 예외객체간의 쓰임새가 모두 고유하지는 않기에 겹치는 부분들이 있고 이런 경우 고민이 될 수 있다.

- 예) 카드 덱을 표현하는 객체가 있고, 인수로 전달한 수 만큼 카드를 뽑아 나눠주는 메서드

  - 덱에 남아 있는 카드 수보다 큰 값을 인수로 전달하면 어떤 예외를 던져야 할까? 

    - ~~~java
      throw new IllegalArgumentException(); // 인수의 값이 너무 크다면
      throw new IllegalStateException(); // 덱 카드 수가 너무 적은 경우
      ~~~

  - 책에서 제시한 일반적인 가이드

    - ~~~
      - 인수 값과 무관하게 실패했을 것이라면 IllegalStateException을 던진다. 
      - 인수 값에 따라 성공할 수 있었다면 IllegalArgumentException을 던진다.
      ~~~

~~~java
public class Cards {
    private final List<Card> deck = new ArrayList<>();

    public static Cards from(int count) {
        List<Card> deck = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            deck.add(new Card(i));
        }
        return new Cards(deck);
    }

    public Cards(List<Card> deck) {
        this.deck.addAll(deck);
    }

    public List<Card> poll(int pickCount) {
        if (pickCount > deck.size()) {
            throw new IllegalArgumentException(); // 인수의 값이 너무 크다면
	          throw new IllegalStateException(); // 덱 카드 수가 너무 적은 경우
        }
        Collections.shuffle(deck);
        return deck.subList(0, pickCount);
    }

    private static class Card {
        int number;
        public Card(int number) {
            this.number = number;
        }
    }
}
~~~





