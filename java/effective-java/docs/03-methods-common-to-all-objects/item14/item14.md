# Item 14. Consider implementing Comparable



### 

Comparable 인터페이스의 유일무이한 메서드인 compareTo를 알아보자.

- Object.equals에 더해서 순서까지 비교할 수 있으며 Generic을 지원한다.
- 자기 자신이 (this)이 compareTo에 전달된 객체보다 작으면 음수, 같으면 0, 크 다면 양수를 리턴한다.
- 반사성, 대칭성, 추이성을 만족해야 한다.
- 반드시 따라야 하는 것은 아니지만 x.compareTo(y) == 0이라면 x.equals(y)가 true여야 한다.



- 여러분도 Comparable을 구현해서 이 인터페이스를 활용하는 수많은 제네릭 알고리즘과 컬렉션의 힘을 누릴 수 있다.
- 사실상 자바 플랫폼 라이브러리의 모든 값 클래스와 **열거타입(아이템 34)이 Comparable**을 구현했다.
- 알파벳, 숫자,연대같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인텊페이스를 구현하자





## compareTo 메서드의 일반규약은 equals 의 규약과 비슷하다.

이 객체와 주어진 객체의 순서를 비교한다. 이 객체가 주어진 객체보다 작으면 음의정수, 같으면 0 크면 양의정수를 를 반환한다.
이 객체와 비교할 수 없는 타입의 객체면 ClassCastException 을 던진다.

- Comparable을 구현한 클래스는 모든 x,y에 대해 sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) 여야 한다. (따라서 x.compareTo(y)는 y.compareTo(x)가 예외를 던질 때에 한해 예외를 던져야 한다). **(대칭성)**

- Comparable을 구현한 클래스는 **추이성을** 보장해야 한다. 즉, (x.compareTo(y)) > 0 && y.compareTo(z) >0) 이면 x.compareTo(z) > 0 이다. **(추이성)**

- Comparable을 구현한 클래스는 모든 z에 대해 x.compareTo(y) == 0 이면 sgn(x.compareTo(z)) == sgn(y.compareTo(z)) 이다. **(일관성)**

- 이번 권고가 필수는 아니지만 꼭 지키는게 좋다. (x.compareTo(y) == 0) == (x.equals(y)) 여야 한다. Comparable을 구현하고 이 권고를 지키지 않는 모든 클래스는 그 사실을 명시해야 한다. 다음과 같이 명시하면 적당할 것이다.

  **(주의: 이 클래스의 순서는 equals 메서드와 일관되지 않다.)**
  
  - **BigDecimal > compareTo은 주석에 다르다고 예시와함께 작성되어있다.**

~~~java
  BigDecimal n1 = BigDecimal.valueOf(23134134);
  BigDecimal n2 = BigDecimal.valueOf(11231230);
  BigDecimal n3 = BigDecimal.valueOf(53534552);
  BigDecimal n4 = BigDecimal.valueOf(11231230);

  // 반사성
  System.out.println(n1.compareTo(n1));

  // 대칭성
  System.out.println(n1.compareTo(n2));
  System.out.println(n2.compareTo(n1));

  // 추이성
  System.out.println(n3.compareTo(n1) > 0);
  System.out.println(n1.compareTo(n2) > 0);
  System.out.println(n3.compareTo(n2) > 0);

  // 일관성
  System.out.println(n4.compareTo(n2));
  System.out.println(n2.compareTo(n1));
  System.out.println(n4.compareTo(n1));
~~~

**hashCode** 규약을 지키지 못하면 해시를 사용하는 클래스와 어울리지 못하듯, compareTo 규약을 지키지 못하면 비교를 활용하는 클래스와 어울리지 못한다.
비교를 활용하는 클래스의 예로는 정렬된 컬렉션인 **TreeSet**, **TreeMap**, 검색과 정렬 알고리즘을 활용하는 유틸리티 클래스인 **Collections** 와 **Arrays** 가 있다.





### compareTo의 마지막 규약은 필수는 아니지만 지키는 게 좋다.

- 동치성 테스트가 equals 와 같다는 말 말이다.
- 만약 안지키면, 동작은 하지만 이 클래스의 객체를 정렬된 컬렉션에 넣으면 해당 컬렉션이 구현한 인터페이스 (Collection, Set,Map 등) 에 정의된 동작과 엇박자를 낼것이다.
- 이 인터페이스들은 equals 메서드의 규약을 따른다고 되어있지만 놀랍게도 정렬된 컬렉션들은 동치성을 비교할 때 equals 대신 compareTo를 사용하기 때문이다. 



- compareTo와 equals가 일관되지 않는 BigDecimal 클래스를 예로 생각해보자.
- 빈 HashSet 인스턴스를 생성한 다음 new BigDecimal("1.0") 과 new BigDecimal("1.00")을 차례로 추가한다.
- 이 두 BigDecimal은 equals 메서드로 비교하면 서로 다르기 때문에 HashSet은 원소를 2개 갖게된다.
- 하지만 **HashSet** 대신 **TreeSet**을 사용하면 원소를 하나만 갖게 된다.
- compareTo 메서드로 비교하면 두 BigDecimal 인스턴스가 똑같기 때문이다. (BigDecimal 문서 참조)

~~~java
// compareTo가 0이라면 equals는 true여야 한다. 실제로는 아닌 경우도 있다.
BigDecimal oneZero = new BigDecimal("1.0");
BigDecimal oneZeroZero = new BigDecimal("1.00");
System.out.println(oneZero.compareTo(oneZeroZero)); // Tree, TreeMap // 0
System.out.println(oneZero.equals(oneZeroZero)); // 순서가 없는 콜렉션 // false

// Tree, TreeMap은 compareTo로 비교해서 데이터를 입력하기 때문에 1개 데이터만 입력된다,
TreeSet<BigDecimal> a = new TreeSet<>();
a.add(oneZero);
a.add(oneZeroZero);
System.out.println(a.size());
Arrays.stream(a.toArray()).forEach(System.out::println);

// 순서없는 컬렉션 : equals로 비교하기 때문에 2개 모두 입력된다.
HashSet<BigDecimal> b = new HashSet<>();
b.add(oneZero);
b.add(oneZeroZero);
System.out.println(b.size() + " " );
Arrays.stream(b.toArray()).forEach(System.out::println);

~~~

- 실제 데이터추가 로직을 살펴보면 TreeSet 은 compareTo, HashSet은 equals, hashcode 로 비교한다.

~~~java
# result
0
false
1
1.0
2 
1.0
1.00
~~~







**Comparable은 타입을 인수로 받는 제네릭 인터페이스 이므로 compareTo메서드의 인수타입은 컴파일타임에 정해진다. (입력인수의 타입을 확인하거나 형변환할 필요가 없다는 뜻이다.)**

~~~java
@Override
public int compareTo(Point point) {
    int result = Integer.compare(this.x, point.x);
    if (result == 0) {
        result = Integer.compare(this.y, point.y);
    }
    return result;
}
~~~

~~~java
@Override
public boolean equals(Object obj) {
    return super.equals(obj);
}
~~~

- compareTo 는 클래스 명이 인자로 입력되지만, equals 는 Object가 입력되어 형변환 및 검사를 해야 한다.





## 자식 클래스에 필드가 추가될 경우

### 방법1. TreeSet 생성자에 Comparator 구현체를 입력한다.

- 방법이 어렵다.

~~~java
public class Point implements Comparable<Point>{

    final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Point point) {
        int result = Integer.compare(this.x, point.x);
        if (result == 0) {
            result = Integer.compare(this.y, point.y);
        }
        return result;
    }
}
~~~

~~~java
public class NamedPoint extends Point {

    final private String name;

    public NamedPoint(int x, int y, String name) {
        super(x, y);
        this.name = name;
    }

    @Override
    public String toString() {
        return "NamedPoint{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        NamedPoint p1 = new NamedPoint(1, 0, "keesun");
        NamedPoint p2 = new NamedPoint(1, 0, "whiteship");

        Set<NamedPoint> points = new TreeSet<>(new Comparator<NamedPoint>() {
            @Override
            public int compare(NamedPoint p1, NamedPoint p2) {
                int result = Integer.compare(p1.getX(), p2.getX());
                if (result == 0) {
                    result = Integer.compare(p1.getY(), p2.getY());
                }
                if (result == 0) {
                    result = p1.name.compareTo(p2.name);
                }
                return result;
            }
        });

        points.add(p1);
        points.add(p2);

        System.out.println(points);
    }

}

~~~

### 방법2. Composition을 이용한다.

- 단순하게 NamedPoint도 Comparable을 생성해서 비교할 수 있어서 직관적이다.

~~~java
public class NamedPoint implements Comparable<NamedPoint> {

    private final Point point;
    private final String name;

    public NamedPoint(Point point, String name) {
        this.point = point;
        this.name = name;
    }

    public Point getPoint() {
        return this.point;
    }

    @Override
    public int compareTo(NamedPoint namedPoint) {
        int result = this.point.compareTo(namedPoint.point);
        if (result == 0) {
            result = this.name.compareTo(namedPoint.name);
        }
        return result;
    }
}
~~~





## 비교자 (Comparable)

### 객체참조 필드가 하나인 비교자 (자바가 제공)

- String에서 제공하는 비교자를 사용해서 구현.

~~~java
public final class CaseInsensitiveString
        implements Comparable<CaseInsensitiveString> {
      public int compareTo(CaseInsensitiveString cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }
...
}
~~~

- CaseInsensitiveString이 Comparable<CaseInsensitiveString>을 구현한 것에 주목하자.
- CaseINsensitiveString의 참조는 CastInsensitiveString 참조와만 비교할 수 있다는 뜻으로, Comparable을 구현할 때 일반적으로 따르는 패턴이다.



### 비교할 필드가 여러종류일 경우

- 클래스에 핵심필드가 여러개라면 어느 것을 먼저 비교하느냐가 중요해진다.
- 가장 핵심적인 필드부터 비교해나가자. 

~~~java
@Override
public int compareTo(PhoneNumber pn) {
    int result = Short.compare(areaCode, pn.areaCode); // 가장 중요한 필드
    if (result == 0)  {
        result = Short.compare(prefix, pn.prefix); // 두번째
        if (result == 0)
            result = Short.compare(lineNum, pn.lineNum); // 세번째
    }
    return result;
}
~~~





### 비교자 생성 메서드를 활용한 비교자

- java8 부터 Comparator 인터페이스에 여러 default, static 메서드가 만들어졌다.
- 이에 대해 비교자 생성메서드를 만들어두고 사용할 수 있다.
- 성능이 조금 느려진다고 하지만, 비교를 많이하는 로직에서는 대안을 찾고 그렇지 않으면 사용해도 좋을 것 같다.

~~~java
// 비교자 생성 메서드를 활용한 비교자
private static final Comparator<PhoneNumber> COMPARATOR =
      comparingInt((PhoneNumber pn) -> pn.areaCode)
              .thenComparingInt(pn -> pn.getPrefix())
              .thenComparingInt(pn -> pn.lineNum);
~~~

~~~java
// API
public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {
    Objects.requireNonNull(keyExtractor);
    return (Comparator<T> & Serializable)
        (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));
}
~~~

~~~java
// API
default Comparator<T> thenComparingInt(ToIntFunction<? super T> keyExtractor) {
    return thenComparing(comparingInt(keyExtractor));
}
~~~





### 기본타입은 래퍼 클래스의 compare로 비교하는게 좋다.

~~~java
System.out.println(Integer.compare(-2147483648, 10));
~~~

~~~sh
# result
-1
~~~

~~~java
// API
public static int compare(int x, int y) {
    return (x < y) ? -1 : ((x == y) ? 0 : 1);
}
~~~

- 내부적으론 부등호로 비교하고 있다.



### 부동소숫점 연산은 BigDecimal을 이용해라

~~~java
int i = 1;
double d = 0.1;
System.out.println(i - d * 9);

BigDecimal bd = BigDecimal.valueOf(0.1);
System.out.println(BigDecimal.valueOf(1).subtract(bd.multiply(BigDecimal.valueOf(9))));
~~~

~~~sh
# result
0.09999999999999998
0.1
~~~







## 정리

- 순서를 고려해야 하는 값 클래스를 작성한다면 꼭 Comparable 인터페이스를 구현하여, 그 인스턴스들을 쉽게 정렬하고 검색하고, 비교기능을 제공하는 컬렉션과 어우러지도록 해야한다.
- compareTo 메서드에서 필드의 값을 비교할 때 <와 >연산자를 쓰지 말아야 한다.
- 그 대신 박싱된 기본타입클래스가 제공하는 정적 compare 메서드나 Comparable 인터페이스가 제공하는 비교자 생성 메서드를 사용하자.



















