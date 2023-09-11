# 인터페이스는 구현하는 쪽을 생각해 설계하라



자바8부터 인터페이스에 default method 를 추가할 수 있게 되었다. (구현가능)

디폴트 메서드를 선언하면 그 인터페이스를 구현한 후 디폴트 메서드를 재정의 하지 않은 모든 클래스에서 디폴트 구현이 쓰이게 된다.
이처럼 자바에도 기존 인터페이스에 메서드를 추가하는 길이 열렸지만 모든 기존 구현체들과 매끄럽게 연동되리라는 보장은 없다.
디폴트 메서드는 구현클래스에 대해 아무것도 모르고 무조건 삽입될 뿐이다.

자바8에서는 핵심 컬렉션 인터페이스들에 다수의 디폴트 메서드가 추가되었다. 주로 람다를 참조하기 위해서다. 
하지만 **생각할 수 있는 모든 상황에서 불변식을 해치지 않는 디폴트 메서드를 작성하기란 어렵다.**



### 자바8 Collection > removeIf method

~~~java
// java.util.Collection.java
// 이 메서드는 주어진 predicate가 true를 반환하는 모든 원소를 제거한다.
default boolean removeIf(Predicate<? super E> filter) {
      Objects.requireNonNull(filter);
      boolean removed = false;
      final Iterator<E> each = iterator();
      while (each.hasNext()) {
          if (filter.test(each.next())) {
              each.remove();
              removed = true;
          }
      }
      return removed;
  }
~~~

- 일반적으로는 사용하기 좋아보이지만**, 멀티스레드에서 안전하지 않은 코드이다.**
-  대표적인 예가 org.apache.commons.collection4.collection.SynchronizaedCollection 이다.
  - 아파치 커먼즈 라이브러리의 이 클래스는 java.util의 Collections.synchronizedCollection 정적 팩터리 메서드가 반환하는 클래스와 비슷하다.
  - 아파치 버전은 클라이언트가 제공한 객체로 락을 거는 능력을 추가로 제공한다.
- 즉 모든 메서드에서 주어진 락 객체로 동기화 한 후 내부 컬렉션 객체에 기능을 위임하는 래퍼클래스**(아이템 18)** 이다.



###  org.apache.commons.collections4.collection.SynchronizedCollection

- 여러 스레드가 공유하는 혼경에서 removeif 는 ConcurrentModificationException 이 발생할 수 있다.
- 4.4 버전에 remoeIf 함수 추가되었다. (4.4 이전에는 재정의하지 않았다.)

~~~xml
<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-collections4 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-collections4</artifactId>
    <version>4.4</version>
</dependency>
~~~

~~~java
// org.apache.commons.collections4.collection.SynchronizedCollection
public class SynchronizedCollection<E> implements Collection<E>, Serializable {
    private static final long serialVersionUID = 2412805092710877986L;
    private final Collection<E> collection;
    protected final Object lock;
		...
    public boolean removeIf(Predicate<? super E> filter) {
        synchronized(this.lock) {
            return this.decorated().removeIf(filter);
        }
    }
   ...
}
~~~

- 4.4 버전 이전에는 Collections.synchronizedCollection 이 반환하는 package-private클래스들은 removeIf를 재정의하고, 이를 호출하는 다른 메서드들은 디폴트 구현을 호출하기 전에 동기화를 하도록 했다.

~~~java
// Collection.SynchronizedCollection
static class SynchronizedCollection<E> implements Collection<E>, Serializable {
   ...
    final Collection<E> c;  
    final Object mutex;
    SynchronizedCollection(Collection<E> c, Object mutex) {
        this.c = Objects.requireNonNull(c);
        this.mutex = Objects.requireNonNull(mutex);
    }
   ...
           @Override
    public boolean removeIf(Predicate<? super E> filter) {
        synchronized (mutex) {return c.removeIf(filter);}
    }
    ...
}			
~~~

- 하지만, 자바 플랫폼에 속하지 않은 제 3의 기존 컬렉션 구현체들은 이런 언어 차원의 인터페이스 변화에대해 모두 수정할 수는 없다.



### 디폴트 메서드는 기본 구현체에 런타임 오류를 일으킬 수 있다.

~~~java
interface Mark {
    default void show() {
        System.out.println("interface show");
    }
}

class Super {
    private void show() {
        System.out.println("Super show");
    }
}

class Sub extends  Super implements Mark {

}

/**
 * *** tried to access private method 에러가 발생한다. ***
 *
 * interface, class 두 리소스가 경합할 때 class쪽을 따른다.
 * 그런데 접근자가 private 이면 interface를 따를거라 생각하지만
 * private 을 호출시도해서 런타임 에러가 발생해버리는 버그에 가까운 일이 발생한다..
 */
public class DefaultMethodBug {
    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.show();
    }
}
~~~

- 기존 인터페이스에 디폴트 메서드로 새 메서드를 추가하는 일은 꼭 필요한 경우가 아니면 피해야 한다.
- 추가하려는 디폴트 메서드가 기존 구현체들과 충돌하지는 않을지 심사숙고 함도 당연하다.
- 반면 새로운 인터페이스를 만드는 경우라면 표준적인 메서드 구현을 제공하는데 유용한 수단이며, 그 인터페이스를 더 쉽게 구현해 활용할 수 있게끔 해준다 **(아이템20)**



### 정리

- **디폴트 메서드라는 도구가 생겼더라도 인터페이스를 설계할 때는 여전히 세심한 주의를 기울야 한다.**
- 최소한 다른방식으로 세가지 이상 구현을 해봐야 한다.
- 또한 각 인터페이스의 인스턴스를 다앙한 작업에 활용하는 클라이언트도 여러개 만들어봐야 한다.
- 인터페이스를 릴리스 한 후라도 결함을 수정하는게 가능할 수 있겠지만, 안일한 생각 마라