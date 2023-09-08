# item18. 상속보다는 컴포지션을 사용하라



상속은 코드를 재사용하는 강력한 수단이지만, 항상 최선은 아니다.
잘못사용하면 오류를 내기 쉬운 소프트웨어를 만들게 된다.
상위 클래스와 하위 클래스를 모두 같은 프로그래머가 통제하는 패키지 안에서라면 상속도 안전한 방법이다.
확장할 목적으로 설계되었고 문서화도 잘 된 클래스(**아이템 19**)도 마찬가지로 안전하다.
하지만 일반적인 구체 클래스를 패키지 경계를 넘어, 즉 다른 패키지의 구체 클래스를 상속하는 일은 위험하다.
상기하자면, 이 책에서의 상속은 (클래스가 다른 클래스를 확장하는) 구현상속을 말한다.
이번 아이템에서 논하는 문제는 (클래스가 인터페이스를 구현하거나 인터페이스가 다른 인터페이스를 확장하는) 인터페이스 상속과는 무관하다.

**메서드 호출과 달리 상속은 캡슐화를 깨뜨린다.**

다르게 말하면, 상위 클래스가 어떻게 구현되느냐에 따라 하위 클래스의 동작에 이상이 생길 수 있다.
상위 클래스는 릴리스마다 내부 구현이 달라질 수 있으며, 그 여파로 코드 한 줄 건드리지 않은 하위 클래스가 오동작할 수 있다는 말이다. 
이러한 이유로 상위 클래스 설계자가 확장을 충분히 고려하고 문서화도 제대로 해두지 않으면 하위 클래스는 상위 클래스의 변화에 말맞춰 수정돼야만 한다.

구체적인 예를 살펴보자. 
우리에게 HashSet을 사용하는 프로그램이 있다.
성능을 높이려면 이 HashSet은 처음 생성 된 이후 원소가 몇 개 더해졌는지 알 수 있어야 한다.
(HashSet의 현재 크기와는 다른 개념이다. 현재 크기는 원소가 제거되면 줄어든다).
그래서 코드 18-1과 같이 변형된 HashSet을 만들어 추가된 원소의 수를 저장하는 변수와 접근자 메서드를 추가했다.
그런다음 HashSet에 원소를 추가하는 메서드인 add와 addAll을 재정의 했다.

~~~java
/**
 * TODO 코드의 기댓값은 3이지만 실제론 6이다.
 *      -> 부모클래스의 addAll는 add를 호출하기에 자식클래스의 add를 호출. (override 했기 때문)
 *      상속의 위험성
 *      예제에서처럼 부모 함수내부에서 어떤 일이 일어나는지 완전히 모르면, side effect가능성이 항상 있다.
 *      부모클래스에 어떤 요소가 추가(변경)하는 함수가 만들어졌을 때, 그 기능이 추가(변경)되었다는 사실 자체를 알기어렵다.
 *      -> 컴파일은 되지만, 런타임중 기능이 바뀌어버림
 *      자식클래스가 정의한 함수 abc가 잇는데, 어느 순간 부모클래스가 같은 이름으로 정의하게 되었다면 코드가 깨진다.
 *
 * TODO 상속은 지양하는게 좋다.
 */
public class InstrumentedHashSet<E> extends HashSet<E> {
    // 추가된 원소의 수
    private int addCount = 0;

    public InstrumentedHashSet() {
    }

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

    public static void main(String[] args) {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(List.of("apple", "banana", "cherry"));
        System.out.println(s.getAddCount());
    }
}

~~~





### Composition

~~~java
/**
 *  TODO ForwardingSet의 위임을 통해 Set을 composition을 한다. 위임을 하기에 decorator pattern 을 사용하고 있기도 하다.
 *       decorator pattern
 *       -> Component: Set
 *          Decorator: ForwardingSet
 *          ContreteDecorator: InstrumentedSet
 *
 *
 *  TODO InstrumentedSet는 Set기능을 사용하기 위해
 *       Set으로 요청을 위임해주는 ForwardingSet class를 상속받았다.
 *       이전예제 (InstrumentedHashSet.java)와 같은 요청이지만, extends 를 하지 않았기때문에
 *       side effect없이 정상 동작한다.
 *
 *  TODO HashSet 에 기능이 추가변경 되더라도 Set 엔 영향이 없다.
 *       Set에 기능이 추가된다면, 컴파일이 안될 것이기에 (Override) 문제가 없다.
 *
 */
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public InstrumentedSet(Set<E> s) {
        super(s);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
    public int getAddCount() {
        return addCount;
    }

    public static void main(String[] args) {
        InstrumentedSet<String> s = new InstrumentedSet<>(new HashSet<>());
        s.addAll(List.of("apple", "banana", "cherry"));
        System.out.println(s.getAddCount());
    }
}
~~~

~~~java
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;
    public ForwardingSet(Set<E> s) { this.s = s; }

    public void clear()               { s.clear();            }
    ....
}
~~~







### 래퍼클래스 중 콜백 SELF 문제

- 콜백함수: 다른함수(A)의 인자로 전달된 함수 (B)로, 해당함수(A) 내부에서 필요한 시점에 호출될 수 있는 함수 (B)를 말한다.

- 래퍼로 감싸고 있는내부 객체가 어떤 클래스(A)의 콜백으로(B) 사용되는 경우에 this를 전달한다면
  해당클래스(A)는 래퍼가 아닌 내부 객체를 호출한다(Self 문제)

~~~java
/**
 * Callback 객체인 Inner class는 인스턴스생성시 Service를 인자로 받는다.
 * inner가 run 메서드를 호출할 때 service 의 run 함수에 self instance 를 매개변수로 넘겨준다.
 * 이후 service에서 inner의 콜백 메서드를 실행하게 된다..
 *
 * Wrapper 로 inner객체를 감싸서 같은 방식으로 진행한다. 하지만 wrapper 의 콜백이 아닌, inner 의 콜백이 실행된다.
 * -> 자기참조를 인자로 전달하기에 발생하는문제이다. 주의해야 한다.
 */
public class App {
    public static void main(String[] args) {
        Service service = new Service();
        Inner inner = new Inner(service);
        inner.run();
        System.out.println("==========================");
        Wrapper wrapper = new Wrapper(inner);
        wrapper.run();
    }
}

~~~

~~~java
// 래퍼클래스가 run() 을통해 내부객체 inner의 run()을 호출한다.
public class Wrapper implements Callback {
    ...
    @Override
    public void call() {
        this.inner.call();
        System.out.println("call wrapper");
    }

    @Override
    public void run() {
        this.inner.run();
    }
}
~~~

~~~java
// inner class 는 run() 호출 시 자기자신 객체를 인자로 입력한다.
// 자신을 감싸는 래퍼클래스 등은 알 수 가 없다.
class Inner implements Callback {
    ...
    @Override
    public void call() {
        System.out.println("call inner");
    }

    @Override
    public void run() {
        this.service.run(this);
    }
}
~~~

~~~java
// callback 객체는 결국 Inner 객체이므로, Inner의 call 메서드가 실행된다.
public class Service {
    public void run(Callback callback) {
        System.out.println("call service");
        callback.call();
    }
}
~~~



~~~sh
# result
call service
call inner
==========================
call service
call inner
~~~



## 정리

상속은 강력하지만 캡슐화를 해친다는 문제가 있다.
상속은 상위 클래스와 하위 클래스가 순수한 is-a 관계일 때만 써야 한다. is-a 관계일 때도 안심할 수만은 없는게, 하위 클래스의 패키지가 상위 클래스와 다르고, 상위 클래스가 확장을 고려해 설계되지 않았다면 여전히 문제가 될 수 있다.

상속의 취약점을 피하려면 상속 대신 컴포지션과 전달을 사용하자.
특히 래퍼 클래스로 구현할 적당한 인터페이스가 있다면 더욱 그렇다. 래퍼 클래스는 하위 클래스보다 견고하고 강력하다.























