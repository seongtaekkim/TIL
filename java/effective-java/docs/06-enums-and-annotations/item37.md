# item37 ordinal 인덱싱 대신 EnumMap을 사용하라





## 인덱싱 예제1

### 1. ordinal() 과 배열을 이용

- 배열은 제네릭과 호환이 안되니 비검사 형변환 필요
- 배열은 인덱스 의미를 알 수 없으니 출력결과에 레이블과 함께 작성 필요
- 배열에 인덱스를 개발자가 직접 작성해야 하니, 예외발생 가능성이 있음.

~~~java
@SuppressWarnings("unchecked")
Set<_01_Plant>[] plantsByLifeCycleArr =
        (Set<_01_Plant>[]) new Set[LifeCycle.values().length];
for (int i = 0; i < plantsByLifeCycleArr.length; i++)
    plantsByLifeCycleArr[i] = new HashSet<>();
for (_01_Plant p : garden)
    plantsByLifeCycleArr[p.lifeCycle.ordinal()].add(p);
// Print the results
for (int i = 0; i < plantsByLifeCycleArr.length; i++) {
    System.out.printf("%s: %s%n", LifeCycle.values()[i], plantsByLifeCycleArr[i]);
}
~~~



### 2. EnumMap 을 이용

- 1보다 더 짧고 명료하다.
- 비검사 형변환을 하지 않는다.
- 출력결과에 레이블을 안달아도 된다. (맵의 키인 열거타입이 그 자체로 출력무용 문자열 제공)
- 배열 인덱스 작성시 오류날 가능성이 없다.
- EnumMap 내부에서 배열을 쓰기 때문에 성능이 비슷하다.

~~~java
Map<LifeCycle, Set<_01_Plant>> plantsByLifeCycle = new EnumMap<>(LifeCycle.class);
for (LifeCycle lc : LifeCycle.values())
    plantsByLifeCycle.put(lc, new HashSet<>());
for (_01_Plant p : garden)
    plantsByLifeCycle.get(p.lifeCycle).add(p);
System.out.println(plantsByLifeCycle);
~~~



### 3. Collector.groupingby 이용

groupingBy 메서드를 아래처럼 구현하면, API에서 정해놓은 HashMap 를 통해 grouping 되므로 EnumMap의 공간,성능 이점이 사라질 수 있다.

~~~java
System.out.println(Arrays.stream(garden)
        .collect(groupingBy(p -> p.lifeCycle)));
~~~

~~~java
// Collectors.java
public static <T, K, A, D>
Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
                                      Collector<? super T, A, D> downstream) {
    return groupingBy(classifier, HashMap::new, downstream);
}
~~~



### 4. Collector.groupingby 와 EnumMap 를 이용

- groupingBy 두번재 인자를 통해 원하는 맵 구현체를 통해 그룹핑할 수 있다.
- EnumMap 의 성능을 그대로 사용할 수 있게 된다.

~~~java
System.out.println(Arrays.stream(garden)
        .collect(groupingBy(p -> p.lifeCycle,
                () -> new EnumMap<>(LifeCycle.class), toSet())));
~~~

~~~java
public static <T, K, D, A, M extends Map<K, D>>
Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> classifier,
                              Supplier<M> mapFactory,
                              Collector<? super T, A, D> downstream) {
    ...
    @SuppressWarnings("unchecked")
    Supplier<Map<K, A>> mangledFactory = (Supplier<Map<K, A>>) mapFactory;
 	  ...
~~~





##  인덱싱 예제2

### Ordinal 배열 사용

- 컴파일러는 ordinal과 배열 인덱스의 관계를 알 수 없다.
- 따라서 열거타입을 수정하면서 TRANSITIONS 를 수정하지 않으면 런타임 에러가 날 수 있다.
- 또 상전이 표는 상태 개수가 늘어나면 제곱으로 커지며 null 도많아진다.

~~~java
public enum _03_Phase {
    SOLID, LIQUID, GAS;

    public enum Transition {
        MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;
        private static final Transition[][] TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };
        public static Transition from(_03_Phase from, _03_Phase to) {
            return TRANSITIONS[from.ordinal()][to.ordinal()];
        }
    }
    public static void main(String[] args) {
        for (_03_Phase src : _03_Phase.values()) {
            for (_03_Phase dst : _03_Phase.values()) {
                Transition transition = Transition.from(src, dst);
                if (transition != null)
                    System.out.printf("%s to %s : %s %n", src, dst, transition);
            }
        }
    }
}
~~~



### EnumMap 사용

- 안쪽 맵은 이전상태와 전이를 연결하고 바깥맵은 이후 상태와 안쪽 맵을 연결한다.
- 전이 전후의 두 사애를 전이 열거타입 Transition의 입력으로 받아, 이 Transition 상수들로 중첩된 EnumMap을 초기화하면 된다.

~~~java
public enum _04_Phase {
    SOLID, LIQUID, GAS;
    public enum Transition {
        MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);

        private final _04_Phase from;
        private final _04_Phase to;
        Transition(_04_Phase from, _04_Phase to) {
            this.from = from;
            this.to = to;
        }

        private static final Map<_04_Phase, Map<_04_Phase, Transition>>
                m = Stream.of(values()).collect(groupingBy(t -> t.from,
                () -> new EnumMap<>(_04_Phase.class),
                toMap(t -> t.to, t -> t,
                        (x, y) -> y, () -> new EnumMap<>(_04_Phase.class))));
        
        public static Transition from(_04_Phase from, _04_Phase to) {
            return m.get(from).get(to);
        }
    }

    public static void main(String[] args) {
        for (_04_Phase src : _04_Phase.values()) {
            for (_04_Phase dst : _04_Phase.values()) {
                Transition transition = Transition.from(src, dst);
                if (transition != null)
                    System.out.printf("%s to %s : %s %n", src, dst, transition);
            }
        }
    }
}
~~~







## 정리

**배열의 인덱스를 얻기 위해 ordinal을 쓰는 것은 일반적으로 좋지 않으니, 대신 EnumMap을 사용하라.**
다차원 관계는 EnumMap<..., EnumMap<...>> 으로 표현하라.
"애플리케이션 프로그래머는 Enum.ordinal을 사용하지 말아야 한다 (아이템35)" 는 일반원칙의 특수한 사례다.
