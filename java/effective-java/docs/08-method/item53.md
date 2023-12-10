# item53 가변인수는 신중히 사용하라



가변인수 메서드는 명시한 타입의 인수를 0개이상 받을 수 있다.

- 가변인수 메서드를 호출하면
- 가장 먼저 인수의 개수와 길이가 같은 배열을 만들고, 인수들을 배열에 저장하여 가변메서드에 전달한다.



~~~java
static int sum(int... args) {
    int sum = 0;
    for (int arg : args)
        sum += arg;
    return sum;
}
~~~

- 인수가 1개 이상이어야 하는경우, 런타임에 예외를 발생시킨다.
  - 문제) 런타임에 발견됨.

~~~java
static int min(int... args) {
    if (args.length == 0)
        throw new IllegalArgumentException("Too few arguments");
    int min = args[0];
    for (int i = 1; i < args.length; i++)
        if (args[i] < min)
            min = args[i];
    return min;
}
~~~

- 아래처럼 필수 인자를 구분하면 컴파일타임에 해결된다.

~~~java
static int min(int firstArg, int... remainingArgs) {
    int min = firstArg;
    for (int arg : remainingArgs)
        if (arg < min)
            min = arg;
    return min;
}
~~~



### API 사용 예

- printf
- reflection



### 문제점

- 가변인수 메서드는 호출될 때마다 배열을 할당,초기화하여 성능이슈가 있다.

- 대안책
  - 메서드 호출이 많은 인자 개수를 다중정의로 정의하면 크게 완화될 수 있다.

~~~java
// EnumSet
public static <E extends Enum<E>> EnumSet<E> of(E e) {}
public static <E extends Enum<E>> EnumSet<E> of(E e1, E e2) {}
...
public static <E extends Enum<E>> EnumSet<E> of(E first, E... rest) {}
~~~





## 정리

인수개수가 일정하지 않은 메서드를 정의해야 한다면 가변인수가 반드시 필요하다.

메서드를 정의할 때 필수 매개변수는 가변인수 앞에 두고, 가변인수를 사용할 때는 성능 문제까지 고려하자.