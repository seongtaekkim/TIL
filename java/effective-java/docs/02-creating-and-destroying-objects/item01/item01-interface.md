# 인터페이스에 정적 메소드



- 기본메소드 (default method)와 정적 메소드를 가질 수 있다.
- 기본메소드
  - 인터페이스에서 메소드 선언 뿐 아니라, 기본적인 구현체까지 제공할 수 있다.
  - 기존의 인터페이스를 구현하는 클래스에 새로운 기능을 추가할 수 있다.
- 정적메소드
  - 자바 9부터 인터페이스에서 private static 메소드도 가질 수 있다.
  - 단, private 필드는 아직도 선언할 수 없다. (private 필드가 필요한 경우 인스턴스와 불가 동반클래스가 필요해 보인다.)



- 인터페이스에서 default, private static method를 정의할 수 있게 되어서 
  인스턴스와 불가  동반 클래스를 만들 필요가 없어졌다. (final class, private 생성자 등)

~~~java
    List<Integer> numbers = new ArrayList();
    numbers.add(100);
    numbers.add(20);
    numbers.add(44);
    numbers.add(3);

    System.out.println(numbers);

    Comparator<Integer> desc = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    };

    numbers.sort(desc.reversed());
    System.out.println(numbers);
~~~



~~~java
@FunctionalInterface
public interface Comparator<T> {

    default Comparator<T> reversed() {
        return Collections.reverseOrder(this);
    }
    ...
~~~

- 인스턴스와 불가 동반 클래스인 Collections 로부터 정적 팩토리 메서드를 수행한다.

~~~java
public class Collections {
    // Suppresses default constructor, ensuring non-instantiability.
    private Collections() {
    }
...
~~~



~~~java
public interface List<E> extends Collection<E> {
	default void sort(Comparator<? super E> c) {
      Object[] a = this.toArray();
      Arrays.sort(a, (Comparator) c);
      ListIterator<E> i = this.listIterator();
      for (Object e : a) {
          i.next();
          i.set((E) e);
      }
  }
  ...
  
~~~









