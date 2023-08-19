# Item 8: Avoid finalizers and cleaners

- 자바에는 두가지 소멸자가 존재한다.
- 두 가지 모두 성능이 좋지 않고, 실행여부가 불확실하다.
- 대안으로 AutoCloseable을 사용하자.



## Finalizer

- gc대상이 된 클래스가 finalize 메서드를 재정의 했다면, Finalizer가 관리하는 대기 큐에 들어가게 된다.
- 이 후 어느 시점에 큐에 있는 자원을 해제하게 된다..
- finalizer는 예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.

~~~java
public class FinalizerIsBad {

    @Override
    protected void finalize() throws Throwable {
        System.out.print("");
    }
}
~~~

~~~java
// 아래 코드는 java11에서 동작하지만, 17에서 동작하지 않음
while(true) {
  i++;
  new FinalizerIsBad();

  if ((i % 1_000_000) == 0) {
      Class<?> finalizerClass = Class.forName("java.lang.ref.Finalizer");
      Field queueStaticField = finalizerClass.getDeclaredField("queue");
      queueStaticField.setAccessible(true);
      ReferenceQueue<Object> referenceQueue = (ReferenceQueue) queueStaticField.get(null);

      Field queueLengthField = ReferenceQueue.class.getDeclaredField("queueLength");
      queueLengthField.setAccessible(true);
      long queueLength = (long) queueLengthField.get(referenceQueue);
      System.out.format("There are %d references in the queue%n", queueLength);
  }
}
~~~

- finalizer 에 직접접근해서 큐 대기열을 직접 확인한다.
- 자원이 계속 해제되지 않고 쌓이다가 해제하는 걸 볼 수 있는데,
- FinalizerIsBad 인스턴스를 생성중이기 때문에 우선순위가 낮은 Finalizer queue 는 해제하지 못하고 대기하여 어느시점에 해제되는 지 알 수 없게 된다.
- finalizer와 cleaner는 즉시 수행된다는 보장이 없다 ([JLS, 12.6](https://docs.oracle.com/javase/specs/jls/se8/html/jls-12.html)). 
- finalizer동작 중 발생한 예외는 무시되며, 처리할 작업이 남았더라도 그 순간 종료된다.[JLS, 12.6]

### Finalizer Attack

**finalizer를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안 문제를 일으킬 수도 있다.**
finalizer 공격원리는 간단한다. 생성자나 직렬화과정 (readObject, readResolve 메서드) 에서 예외가 발생하면, 이 생성되다 만 객체에서 악의적인 하위 클래스의 finalizer가 수행될 수 있게 된다. [link](./item08-finalizer-attack.md)





## Cleaner

- 자바9에서는 finalizer를 deprecated 로 지정하고 cleaner를 그 대안으로 소개하였다. (java API 에서는 사용함)
- 하지만 여전히 실행시점 등이 불확실하다.
- Cleaner 는 내부적으로 PhantomReference 를 사용하고 있기에 그 사용 방법이 비슷하다.

~~~java
public class BigObject {

    private List<Object> resource;

    public BigObject(List<Object> resource) {
        this.resource = resource;
    }

    public static class ResourceCleaner implements Runnable {

        private List<Object> resourceToClean;

        public ResourceCleaner(List<Object> resourceToClean) {
            this.resourceToClean = resourceToClean;
        }

        @Override
        public void run() {
            resourceToClean = null; // 내부객체의 Strong 연결을 끊는다.
            System.out.println("cleaned up.");
        }
    }
}
~~~

~~~java
public static void main(String[] args) throws InterruptedException {
   // cleaner 생성
   Cleaner cleaner = Cleaner.create();

   
    List<Object> resourceToCleanUp = new ArrayList<>();
    BigObject bigObject = new BigObject(resourceToCleanUp);
    // GC로 해제될 오브젝트와 해제코드가 담긴 Runnable객체 인자로 넘겨준다.
    cleaner.register(bigObject, new BigObject.ResourceCleaner(resourceToCleanUp));

    bigObject = null;
    System.gc(); // GC호출 시 실행됨.
    Thread.sleep(3000);
}
~~~

- outerclass 리소스를 참조하면 안된다. (소멸 작업 중 문제 발생)
- innerclass로 정의하려면 static 으로 정의해야 함.








## Cleaner (자바8이하 Finalizer) 의 사용처

### 1. 자원의 소유자가 close메서드를 호출하지 않는것에 대한 안정망 역할이다.

- cleaner나 finalizer가 즉시 (혹은 끝까지) 호출되리라는 보장은 없지만, 클라이언트가 하지 않은 자원회수를 늦게라도 해주는 것이 아예 안하는 것보다는 나으니 말이다.
- 이런 안전망 역할의 finalizer를 작성할때는 그럴만한 값어치가 있는 지 심사숙고하자.
- 자바 라이브러리의 일부 클래스는 안전망 역할의 finalizer를 제공한다. (ThreadPoolExecutor) 

### 2. native peer 와 연결된 객체에서 사용한다.
- native peer란 일반 자바객체가 네이티브메서드를 통해 기능을 위임한 네이티브 객체를 말한다.
- native peer는 자바 객체가 아니니 GC는 그 존재를 알지 못한다.
- 그 결과 자바피어를 회수 할 때 네이티브객체까지 회수하지 못한다.
- cleaner나 finalizer가 나서서 처리하기에 적당한 작업이다.
- 단, 성능저하를 감당할 수 있고, 네이티브 피어가 심각한 자원을 가지고 있지 않을 때에만 해당된다.
- 성능저하를 감당할 수 없거나 네이티브피어가 사용하는 자원을 즉시 회수해야 한다면 앞서 설명한 **close** 메서드를 사용해야 한다.



## AutoCloseable

- 확실하게 객체를 소멸시킬 수 있다.
- AutoCloseable을 구현해주고, 클라이언트에서 인스턴스를 다 쓰고 나면 close메서드를 호출하면 된다. (일반적으로 예외가 발생해도 제대로 종료되도록 try-with-resources를 사용해야 한다. 아이템 9)
- AutoCloseable 혹은 Closeable를 implements 하면 사용할 수 있다.

~~~java
try(AutoClosableIsGood good = new AutoClosableIsGood("")) {
    // TODO 자원 반납 처리가 됨.
}
~~~

~~~java
public class AutoClosableIsGood implements AutoCloseable {

    private BufferedReader reader;

    public AutoClosableIsGood(String path) {
        try {
            this.reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(path);
        }
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

~~~

![스크린샷 2023-08-19 오전 9.36.53](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 9.36.53.png)

### AutoCloseable & Closeable

- close() 정의 시 예외클래스가 다르다
- AutoCloseable : Exception
- Closeable : IOException

- close 메서드의 예외처리 방법은 여러가지가 있지만, 멱등성을 만족해야 한다.

- ~~~
  1. throw exception
  2. catch exception
  3. catch 후 throw new runtimeException 등으로 처리.
  ~~~

  - close 를 여러번 호출하지 않도록 한다.





### 안정망 테스트

- AutoCloseable을 구현한 클래스가 있다. 이 클래스는 try-with-resource 를 사용하면 close()를 구현하게 설계되어 있다.
- 하지만 클라이언트가 try-with-resource를 사용하지 않을경우, 불확실하지만 자원해제의 기회를 주기 위해 Cleaner를 구현한 것이다.

~~~java
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    // 청소가 필요한 자원. 절대 Room을 참조해서는 안 된다!
    private static class State implements Runnable {
        int numJunkPiles; // Number of junk piles in this room

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        // close 메서드나 cleaner가 호출한다.
        @Override public void run() {
            System.out.println("방청소!");
            numJunkPiles = 0;
        }
    }

    // 방의 상태. cleanable과 공유한다.
    private final State state;

    // cleanable 객체. 수거 대상이 되면 방을 청소한다.
    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);
    }

    @Override public void close() {
        cleanable.clean();
    }
}
~~~

~~~java
public static void main(String[] args) {
    try (Room myRoom = new Room(7)) {
        System.out.println("안녕~");
    }
}
~~~

- 로직이 끝나자 close()가 호출되어 자원이 해제된다.

~~~java
public static void main(String[] args) {
    new Room(99);
    System.out.println("Peace out");
    System.gc();
}
~~~

- gc()를 명시적으로 호출하지 않을 경우 해제되지 않는다.

























