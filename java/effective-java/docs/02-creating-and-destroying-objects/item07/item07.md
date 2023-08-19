# Item 7: Eliminate obsolete object references



### 메모리누수 예제 ([link](.item07-gc.md))

~~~java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    return elements[--size];
}
~~~

- 사용자는 pop한 객체를 사용하지 않는다는 걸 알지만, 프로그램은 아직 결합되어 있기 때문에 GC대상이 아니다.

~~~java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
}
~~~

- 해당 참조를 다 썼을 대 null처리 (참조해제) 하기되면 GC대상이 된다.



## Null 처리

- 다 쓴 참조를 null처리하면 다른 이점도 따라온다.
- 만약 null처리한 참조를 실수로 사용하려 하면 프로그램(thread)은 즉시 **npe를 던지며 종료된다.**
  - null처리 하지 않으면 잠재적 에러가 발생 할 수 있다.

- 이런 문제를 접한 프로그래머는 객체사용 후 null처리를 전부 진행하게 될 수 있다.
  하지만 그 행위는 바람직하지 않다. 코드가 지저분해 질 뿐이다.
  **객체참조를 Null처리하는 일은 예외적인 경우여야 한다.**

### Null을 리턴하는 방법

~~~java
// 1
return null; // 호출자에서 null 검사를 진행해야 한다.
// 2
throw new IllegalMonitorStateException(); // 체크exception 은 리소스를 소모한다.
                                          // 꼭 필요한 경우가 아니면 사용하지 않는다.
// 3
Optional.empty();
~~~

### Optional Test

~~~java
@Test
void npe() {
    Channel channel = new Channel();
    Optional<MemberShip> optional = channel.defaultMemberShip();
    System.out.println(optional.isEmpty());
    optional.ifPresent(MemberShip::hello);
}
~~~

- return type으로 Optional을 사용하면, null검사를 하지 않아도 쉽게 로직을 전개할 수 있다.

### Optional 주의사항

~~~java
public void setProgress(Optional<Progress> progress) {
    if (progress != null)
        progress.ifPresent(p-> this.progress = p);
}
~~~

- 파라메터에 Optional 은 지양해야 한다.
- null처리를 객체, Optional 두번 다 진행해야 하므로 낭비다.



## Scope

다 쓴 참조를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 유효범위(scope) 밖으로 밀어내는 것이다.
여러분이 변수의 범위를 취소가 되게 정의했다면(아이템 57) 이 일은 자연스럽게 이뤄진다.

- **scope을 벗어나면 gc 대상이 된다.**

- ~~~java
  // method1() 동작이 끝나면 Object는 gc 대상이 된다.
  public mothod1() {
    Object o = new Object();
  }
  ~~~

  





## 캐시 역시 메모리누수를 일으키는 주범이다.

- 객체참조를 캐시에 넣고 나서, 이 사실을 까맣게 잊은 채 그 객체를 다 쓴 뒤에도 한참을 그냥 놔두는 일을 자주 접할 수 있다. 
- 캐시 외부에서 키를 참조하는 동안만 엔트리가 살아있는 캐시가 필요한 상황이라면 **WeakHashMap** 를 사용하자.
- 단 WeakHashMap은 이러한 상황에서만 유용하다.

~~~java
public class PostRepository {

private Map<CacheKey, Post> cache;

public PostRepository() {
    this.cache = new WeakHashMap<>();
}
...
}
~~~

~~~java
@Test
void cache() throws InterruptedException {
    PostRepository postRepository = new PostRepository();
    CacheKey key1 = new CacheKey(1);
    postRepository.getPostById(key1);

    assertFalse(postRepository.getCache().isEmpty());

    key1 = null;
    System.gc();
    Thread.sleep(3000);

    assertTrue(postRepository.getCache().isEmpty());
}
~~~

- key1 객체가 strong 결합을 잃었을 때, WeakHashMap객체는 gc 대상이 되므로, 해제가 된다.

~~~java
@Test
void cache() throws InterruptedException {
    PostRepository postRepository = new PostRepository();
		Integer key1 = 1;
    postRepository.getPostById(key1);

    assertFalse(postRepository.getCache().isEmpty());

    key1 = null;
    System.gc();
    Thread.sleep(3000);

    assertTrue(postRepository.getCache().isEmpty());
}
~~~

- primitive type, String type은 jvm 어딘가에 캐싱되어 있어서 null을 명시해도 gc대상이 되기 어렵다고 한다.
  따라서 위와같은 객체는 커스텀객체로 래핑해서 사용하는게 좋다.



### 캐싱 데이터 메머리해제 유효기간

- 캐시를 만들 때 보통은 캐시 에트리의 유효기간을 정확히 정의하기 어렵기 때문에 시간이 지날수록 엔트리의 가치를 떨어뜨리는 방식을 흔히 사용한다.
- 이런 방식에서는 쓰지 않는 엔트리를 이따금 청소해야 한다. (ex : **[ScheduledThreadPoolExecutor](./item07-excutor.md)**) 같은 백그라운드 스레드를 활용하거나 캐시에 새 엔트리를 추가할 때 부수 작업으로 수행하는 방법이 있다.
- **LinkedHashmap**은 **removeEldestentry** 메서드를 써서 후자의 방식으로 처리한다. **(추가학습 필요)**
- 더 복잡한 캐시를 만들고 싶다면 [ **java.lang.ref** ](./item07-reference.md)패키지를 직접 활용해야 할 것이다.





### **메모리누수: listener 혹은 callback**

- 클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면, 계속 쌓여갈 것이다.

- 이럴 때 콜백을 약한참조 (weak reference)로 저장하면 GC가 즉시 수거해 간다.

~~~java
public class ChatRoom {

    private List<WeakReference<User>> users;

    public ChatRoom() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        this.users.add(new WeakReference<>(user));
    }

    public void sendMessage(String message) {
        users.forEach(wr -> Objects.requireNonNull(wr.get()).receive(message));
    }

    public List<WeakReference<User>> getUsers() {
        return users;
    }
  ...
~~~

**weakHashMap 학습**

callback (https://myhappyman.tistory.com/106) 예제를 WeakReference 로 만들어보자.







## 정리

메모리누수는 겉으로 잘 드러나지 않아 시스템에 수년간 잠복하는 사례도 있다.
이런 누수는 철저한 코드리뷰나 **힙 프로파일러** 같은 디버깅 도구를 동원해야만 발견되기도 한다.
그래서 이런 종류의 문제는 예방법을 익혀두는 것이 중요하다.

















