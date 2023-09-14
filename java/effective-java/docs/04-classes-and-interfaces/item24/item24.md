# 멤버클래스는 되도록 static 으로 만들어라

## 중첩클래스?

- 중첩클래스란 다른 클래스 안에 정의된 클래스를 말한다. 
- 중첩클래스는 자신을 감싼 바깥 클래스에서만 쓰여야 하며, 그 외의 쓰임새가 있다면 톱레벨 클래스로 만들어야 한다.
- 중첩클래스의 종류는 정적맴버 클래스, 비정적 맴버클래스, 익명클래스, 지역클래스다.



## 정적 멤버 클래스

- 다른 클래스 안에 선언되고, 바깥 클래스의 private 멤버에도 접근할 수 있다.
- 다른 정적 멤버와 같은 접근 규칙을 적용 받는다
  - private로 선언하면 바깥 클래스에서만 접근 가능.

> 정적멤버클래스는 흔히 바깥 클래스와 함께 쓰일 때만 유용한 public 도우미 클래스로 쓰인다.
> 계산기가 지원하는 연산종류를 정의하는 열거 타입을 예로 생각해 보자(**아이템34)**
>
> Operation 열거타입은 Calcurator 클래스의 public 정적 멤버 클래스가 되어야 한다.
> 그러면 Calcurator의 클라이언트에서 Calcurator.Operation.PLUS 같은 형태로 참조 가능하다.



정적 멤버 클래스와 비정적 멤버 클래스의 구문사 차이는 단지 static 유무지만 의미차이는 크다.

- 비정적 멤버클래스의 인스턴스는 바깥 클래스의 인스턴스와 암묵적으로 연결된다.
- 비정적 멤버 클래스의 인스턴스 메서드에서 정규화된 this를 사용해 바깥 인스턴스의 메서드를 호출하거나 바깥 인스턴스의 참조를 가져올 수 있다.
- 정규화된 this란 클래스명.this 형태로 바깥 클래스의 이름을 명시하는 용법을 말한다.
- 따라서 개념상 중첩 클래스의 인스턴스가 바깥 인스턴스와 독립적으로 존재할 수 있다면 **정적 멤버 클래스**로 만들어야 한다.
- 비정적 멤버클래스는 바깥 인스턴스 없이는 생성할 수 없기 때문.

비정적 멤버클래스의 인스턴스와 바깥 인스턴스 사이의 관계는 멤버클래스가 인스턴스화 될 때 확립되며 더이상 변경할 수 없다.
이 관계는 바깥 클래스의 인스턴스 메서드에서 비정적 멤버 클래스의 생성자를 호출할 때 자동으로 만들어지는게 보통이지만
드물게는 직접 **바깥 인스턴스의 클래스.new Member Class(args)** 를 호출해 수동으로 만들기도 한다.

**이 관계정보는 비정적멤버클래스의 인스턴스 안에 만들어져 메모리 공간을 차지하며, 생성시간도 더 걸린다.**



## 비정적 맴버 클래스

- 어댑터를 정의할 때 자주 쓰인다.
- 즉, 어떤 클래스의 인스턴스를 감싸 마치 다른 클래스의 인스턴스처럼 보이게 하는 뷰로 사용하는 것이다.
- Map 인터페이스의 구현체들은 보통 (keySet, entrySet, values 메서드가 반환하는) 자신의 컬렉션 뷰를 구현할 때 비정적 멤버 클래스를 사용한다.

~~~java
public class OuterclassWithIterator<E> extends AbstractSet<E> {
    @Override public Iterator<E> iterator() {
        return new MyIterator();
    }
    @Override public int size() {
        return 0;
    }
    private class MyIterator implements Iterator<E> {
        @Override public boolean hasNext() {
            return false;
        }
        @Override public E next() {
            return null;
        }
    }
}
~~~

**멤버클래스에서 바깥 인스턴스에 접근할 일이 없다면 무조건 static을 부여서 정적멤버 클래스로 만들자.**

- static을 생략하면 바깥 인스턴스로의 숨은 외부 참조를 갖게 된다. 
- 앞서도 이야기 했듯 이 참조를 저장하려면 **시간과 공간**이 소비된다.
- 더 심각한 문제는 GC가 바깥클래스의 인스턴스를 수거하지 못하는 **메모리누수가** 생길 수 있다는 점이다 **(아이템 7)**
  참조가 눈에 보이지 않으니 문제의 원인을 찾기 어렵다.



## **private 정적 멤버 클래스**

- private 정적 멤버 클래스는 흔히 바깥 클래스가 표현하는 객체의 한 부분을 나타낼 때 쓴다.
- 키,값을 매핑하는 Map인스턴스는 각각의 키값에 대한 엔트리 메서드들 (getValue, getKey, setValue)은 맵을 직접 사용하지는 않는다.
- 따라서 엔트리들 비정적 멤버 클래스로 표현하는 것은 낭비고, private 정적 멤버 클래스가 가장 알맞다.

~~~java
public class StaticClass {
    private static int number = 10;
    static private class InnerClass {
        void doSomething() {
            System.out.println(number);
        }
    }
    public static void main(String[] args) {
        InnerClass innerClass = new InnerClass();
        innerClass.doSomething();
    }
}
~~~

- 엔트리를 선언할 때 실수로 static 을 빠뜨려도 동작하겠지만 모든 엔트리가 바깥 맵으로의 참조를 갖게되어 공간과 시간을 낭비할 것이다.
- 멤버클래스가 공개된 클래스의 public,protected 멤버라면 정적이냐 아니냐는 더 중요해진다.
- 멤버 클래스 역시 공개 API 가되니 혹시 향후 static 으로변경되면 호환성이 깨진다.



## 익명클래스

- 익명클래스는 내부 클래스이다.
- 쓰이는 시점에 선언과 동시에 인스턴스가 만들어진다.
- 비정적인 문맥에서 사용될 때에만 바깥 클래스의 인스턴스를 참조할 수 있다.
- 정적 문맥에서라도 상수 변수 이외의 정적 맴버는 가질 수 없다.
- 즉 상수표현(JLS, 4.12.4)을 위해 초기화된 final기본타입과 문자열 필드만 가질 수 있다.

자바가 람다를 지원하기 전에는 즉석에서 작은 함수 객체나 처리 객체를 만드는데 익명 클래스를 주로 사용했다.
이제는 람다가 한다 (**아이템 42**)
정적팩터리메서드 만들 때 사용한다 (아래 예시)

~~~java
public class AnonymousEx {
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);

        return new AbstractList<>() {
            @Override public Integer get(int i) {
                return a[i];
            }

            @Override public Integer set(int i, Integer val) {
                int oldVal = a[i];
                a[i] = val;
                return oldVal;
            }

            @Override public int size() {
                return a.length;
            }
        };
    }
}
~~~

### 익명클래스는 제약

- 선언한 지점에서만 인스턴스를 만들 수 있고, instanceof 검사, 클래스 이름이 필요한 작업은 수행할 수 없다.
- 여러 인터페이스를 구현 못하고 인터페이스를 구현하면서 상속을 할 수 없다.
- 익명클래스는 표현식 중간에 등장하므로 짧지 않으면 가독성이 떨어진다.





## 지역 클래스

- 가장 드물게 사용한다.
- 지역클래스는 지역변수를 선언 할 수 있는곳 어디든 사용 가능하고 유효범위도 지역변수와 같다.
- 비정적 문맥에서 사용될 때만 바깥 인스턴스를 참조할 수 있다.
- 정적맴버는 가질수 없고 가독성을 위해 짧게 작성되어야 한다.

~~~java
public class LocalClassEx {

    private int number = 10;

    void doSomething() {
        class LocalClass {
            private void printNumber() {
                System.out.println(number);
            }
        }

        LocalClass localClass = new LocalClass();
        localClass.printNumber();
    }

    public static void main(String[] args) {
        LocalClassEx myClass = new LocalClassEx();
        myClass.doSomething();
    }
}
~~~





## 정리

중첩클래스에는 네가지가 있으며, 각각의 쓰임이 다르다.
메서드 밖에서도 사용해야 하거나 메서드 안에 정의하기엔 너무 길다면 멤버클래스로 만든다.
멤버클래스의 인스턴스 각각이 바깥 인스턴스를 참조한다면 비정적으로, 그렇지 않으면 정적으로 만들자.
중첩클래스가 한 메서드 안에서만 쓰이면ㄷ서 그 인스턴스를 생성하는 지점이 단 한곳이고 해당 타입으로 쓰기에 적합한 클래스나 인터페이스가 이미 있다면 익명 클래스로 만들고, 그렇지 않으면 지역클래스로 만들자.