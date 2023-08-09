# item03. Enforce the singleton property with a private constructor or an enum type



- 싱글턴이란 인스턴스를 오직 하나만 생성할 수 잇는 클래스를 말한다.

- 싱글턴의 전형적인 예로는 함수(<span style="color:#ffbce0">아이템 24</span>)와 같은 무상태 객체나 설계상 유일해야 하는 시스템 컴포넌트를 들 수 있다.





## private 생성자 + public static final 필드

~~~java
public class Elvis {

    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
~~~

- 장점, 간결하고 싱글턴임을 API에 들어낼 수 있다. 
- private 생성자는 public satic final 필드인 Elvis.INSTANCE를 초기화 할 때 딱 한번만 호출된다.
- Elvis 클래스가 초기활될 때 만들어진 인스턴스가 전체 시스템에서 하나뿐임이 보장된다.



### 이슈1. 싱글톤을 사용하는 클라이언트 테스트하기 어려워진다. 

- 클래스

```java
public class Elvis implements IElvis {

    public static final Elvis INSTANCE = new Elvis();
    private static boolean created;
...
```

~~~java
public class Concert {

    private IElvis elvis;

    public Concert(IElvis elvis) {
        this.elvis = elvis;
    }
...
~~~

~~~java
@Test
void perform() {
    Concert concert = new Concert(new MockElvis());
    concert.perform();
		...
}
~~~



### 이슈2. 리플렉션(<span style="color:#ffbce0">아이템 65</span>)으로 private 생성자를 호출할 수 있다. 

~~~java
try {
      Constructor<Elvis> defaultConstructor = Elvis.class.getDeclaredConstructor();
      defaultConstructor.setAccessible(true);
      Elvis elvis1 = defaultConstructor.newInstance();
      Elvis elvis2 = defaultConstructor.newInstance();
      Elvis.INSTANCE.sing();
  } catch (...) {
		...
  }
~~~

- setAccessible 메서드로 private 기본생성자에 접근해서 새로운 인스턴스를 생성할 수 있다.

~~~java
    private static boolean created;

    private Elvis() {
        if (created) {
            throw new UnsupportedOperationException("can't be created by constructor.");
        }

        created = true;
    }
~~~

- 위와 같이, 최초 인스턴스를 생성한 이후에는 RuntimeExecption 을 던져서 싱글턴을 유지할 수 있다.



### 이슈3. 역직렬화 할 때 새로운 인스턴스가 생길 수 있다.

- [<span style="color:#ffbce0">serialization </span>](./item03-serialization,md)

~~~java
try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("elvis.obj"))) {
    out.writeObject(Elvis.INSTANCE);
} catch (IOException e) {
    e.printStackTrace();
}

try (ObjectInput in = new ObjectInputStream(new FileInputStream("elvis.obj"))) {
    Elvis elvis3 = (Elvis) in.readObject();
    System.out.println(elvis3 == Elvis.INSTANCE);
} catch (IOException | ClassNotFoundException e) {
    e.printStackTrace();
}
~~~

- 역직렬화 할 때, 새로운 인스턴스가 생기게 된다.

~~~java
private Object readResolve() {
    return INSTANCE;
}
~~~

- 역직렬화 과정에서 호출되는 함수이다.
- 역직렬화 대상 class 에 readResolve 함수(<span style="color:#ffbce0">아이템 89</span>)를 정의해서 싱글턴 인스턴스를 리턴하면, 같은 인스턴스가 유지된다.





## private 생성자 + 정적 팩터리 메서드

**API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다.**

~~~java
public class Elvis implements Singer {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { }
    public static Elvis getInstance() { return INSTANCE; }
    ...
}
~~~

- new Elvis(); 를 다른 클래스로 명명해도 된다. (API 변경X)





**정적 팩터리를 제네릭(<span style="color:#ffbce0">아이템 30</span>) 싱글턴 팩터리로 만들 수 있다.**

~~~java
public class MetaElvis<T> {

    private static final MetaElvis<Object> INSTANCE = new MetaElvis<>();

    private MetaElvis() { }

    @SuppressWarnings("unchecked")
    public static <E> MetaElvis<E> getInstance() { return (MetaElvis<E>) INSTANCE; }

    public void say(T t) {
        System.out.println(t);
    }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    public static void main(String[] args) {
        MetaElvis<String> elvis1 = MetaElvis.getInstance();
        MetaElvis<Integer> elvis2 = MetaElvis.getInstance();
        System.out.println(elvis1);
        System.out.println(elvis2);
        elvis1.say("hello");
        elvis2.say(100);
    }
}
~~~

- 타입이 다른 인스턴스는 equals 가 같음을 알 수 있지만, == 는 타입이 다르므로 컴파일에러가 발생한다.



**정적 팩터리의 메서드 참조를 공급자(Supplier(<span style="color:#ffbce0">아이템 43</span>)(<span style="color:#ffbce0">아이템 44</span>))로 사용할 수 있다**.

[<span style="color:#ffbce0">functionalInterface</span>](./item03-functional.md)

~~~java
public class Concert {

    public void start(Supplier<Singer> singerSupplier) {
        Singer singer = singerSupplier.get();
        singer.sing();
    }

    public static void main(String[] args) {
        Concert concert = new Concert();
      
        concert.start(new Supplier<Singer>() {
            @Override
            public Singer get() {
                return Elvis.getInstance();
            }
        });
        concert.start(() -> Elvis.getInstance());

        Supplier<Elvis> getInstance = Elvis::getInstance;
        concert.start(Elvis::getInstance);

    }
}
~~~

- getInstance() 함수는 인자 없이 리턴 인스턴스가 하나 있는 형태라서 Supplier 타입으로 래핑하여 사용할 수 있다.

- Singer 인터페이스를 래핑한 Supplier 인자를 받는 start 함수를 호출하기 위해

  - 익명클래스
  - 람다표현식
  - 메소드레퍼런스

  세가지 방식으로 인자를 작성할 수 있다.





## 열거 타입 

~~~java
public enum Elvis implements IElvis {
    INSTANCE;
~~~

- 가장 간결한 방법이며 직렬화와 리플렉션에도 안전하다.

- 대부분의 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.

- 클래스를 상속해서 만들어야 한다면 eum은 사용할 수 없다.



























































