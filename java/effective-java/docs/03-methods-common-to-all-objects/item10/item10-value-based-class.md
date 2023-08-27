

# value based class



- immutable 해야한다.
- 식별자가 없다. 데이터가 객체의 식별 대상이 된다.
- 식별자가 아니라 인스턴스가 가지고 있는 상태를 기반으로 equals, hashCode, toString을 구현한다. 
- == 이 아니라 equals를 사용해서 동등성을 비교한다. 
- 동일한(equals) 객체는 상호교환 가능한다. 



### value based class 구현

~~~java
public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
~~~

- x, y가 같다면 객체가 같다고 보는 것이다. 인스턴스 끼리의 == 등으로 판단하는게 아니다.



### record

~~~java
public record Point(int x, int y) {}
~~~

~~~java
public static void main(String[] args) {
    Point p1 = new Point(1, 0);
    Point p2 = new Point(1, 0);
    System.out.println(p1.equals(p2));
    System.out.println(p1);

    System.out.println(p1.x());
    System.out.println(p1.y());
}
~~~

- Object 에서 제공해주는 equals, hashcode 로 충분하다.





### [Value-based Classes by oracle](https://docs.oracle.com/javase/8/docs/api/java/lang/doc-files/ValueBased.html) 

Some classes, such as `java.util.Optional` and `java.time.LocalDateTime`, are *value-based*. Instances of a value-based class:

- are final and immutable (though may contain references to mutable objects);
- have implementations of `equals`, `hashCode`, and `toString` which are computed solely from the instance's state and not from its identity or the state of any other object or variable;
- make no use of identity-sensitive operations such as reference equality (`==`) between instances, identity hash code of instances, or synchronization on an instances's intrinsic lock;
- are considered equal solely based on `equals()`, not based on reference equality (`==`);
- do not have accessible constructors, but are instead instantiated through factory methods which make no committment as to the identity of returned instances;
- are *freely substitutable* when equal, meaning that interchanging any two instances `x` and `y` that are equal according to `equals()` in any computation or method invocation should produce no visible change in behavior.

A program may produce unpredictable results if it attempts to distinguish two references to equal values of a value-based class, whether directly via reference equality or indirectly via an appeal to synchronization, identity hashing, serialization, or any other identity-sensitive mechanism. Use of such identity-sensitive operations on instances of value-based classes may have unpredictable effects and should be avoided.







### 참고

- https://bugs.openjdk.org/browse/JDK-8252181
  - value base class list
- https://cr.openjdk.java.net/~jrose/values/values-0.html (자바에 레코드가 필요한 이유)
- [DDD(Domain Driven Design)](https://java-boy.tistory.com/91) 공부 필요







