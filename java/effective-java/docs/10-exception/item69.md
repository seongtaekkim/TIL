# item69 예외는 진짜 예외 상황에만 사용하라



### 반복문 경계검사를 checked exception 으로 처리하는 예시의 문제점

~~~java
try {
  int i=0;
  while (true)
    	range[i++].climb();
} catch(ArrayIndexOutOfBoundsException e) {}
~~~

JVM은 배열에 접근할 때마다 경계를 넘지 안흔지 검사하는데, 일반적인 반복문도 배열 경계에 도달하면 종료한다.

이 검사를 반복문에도 명시하면 체크로직이 중복되므로 생략한것이다.  해당문장이 잘못된이유를 아래 3가지로 나열하였다.

1. 예외는 예외상황에 쓸 용도로 설계되었으므로 JVM 구현자 입장에서는 명확한 검사만큼 빠르게 만들어야 할 동기가 약하다
   (최적화가 안되었을 수 있다.)
2. 코드가 try-catch 블록 안에 있으면 JVM 최적화가 제한된다.
3. 배열 순회 표준 관용구는 중복검사를 안한다 (JVM이 최적화 수행)



### 위 예제는 성능상으로도 좋지 않다.

- 1번예제는 checked exception 비용이 존재하기 때문.

~~~java
/**
 * 1번예제 시간) 62569
 * 2번예제 시간) 8255
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        Mountain[] range = new Mountain[100];
        for (int i=0 ; i<100 ; i++)
            range[i] = new Mountain();

        /**
         * 1번 예제
         */
        long start = System.nanoTime();
        try {
            int i=0;
            while (true)
                range[i++].climb();
        } catch(ArrayIndexOutOfBoundsException e) {
        }
        long end = System.nanoTime();
        System.out.println((end-start));

        /**
         * 2번 예제
         */
        start = System.nanoTime();
        for (Mountain m : range) {
            m.climb();
        }
        end = System.nanoTime();
        System.out.println((end-start));
    }
}
~~~



### API의 잘못된 설계로 인해 발생할 수 있는 문제점

- 상태의존적 메서드(next())가 있는데,  상태검사메서드(hasNext())가 없는경우, 이전에 1번예제와 같은 문제가 있을 수 있다.

~~~java
/**
 * API설계가 잘못된 케이스
 * - 상태의존적 메서드(next())는 상태검사메서드(hasNext())와 같이 제공되어야 한다.
 *
 * 1번예제 시간) 48312
 * 2번예제 시간) 3482
 */
public class Test2 {
    public static void main(String[] args) throws InterruptedException {

        List<Foo> list = new ArrayList<>();
        list.add(new Foo(1));
        list.add(new Foo(2));

        /**
         * 1번 예제
         */
        long start = System.nanoTime();
        try {
            Iterator<Foo> i = list.iterator();
            while (true) {
                Foo foo = i.next();
//                System.out.println(foo);
            }
        } catch(NoSuchElementException e) {
        }
        long end = System.nanoTime();
        System.out.println((end-start));

        /**
         * 2번 예제
         */
        start = System.nanoTime();
        for (Iterator<Foo> i = list.iterator() ; i.hasNext() ;) {
            Foo foo = i.next();
//            System.out.println(foo);
        }
        end = System.nanoTime();
        System.out.println((end-start));
    }
}

~~~



### 상태검사메서드 대신 사용할 수 있는 선택지

# #####예시 만들어야 한다 지금 생각이아난ㅁ##



1. 외부 동기화 없이 여러 스레드가 동시에 접근할 수 있거나 외부 요인으로 상태가 변할 수 있다면 **옵셔널이나 특정 값을 사용한다.**
   상태 검사 메서드와 상태 의존적 메서드 호출 사이에 객체의 상태가 변할 수 있기 때문이다.
2. 성능이 중요한 상황에서 상태 검사 메서드가 상태 의존적 메서드의 작업 일부를 중복수행한다면 옵셔널이나 특정 값을 선택한다.
3. 다른 모든 경우엔 상태 검사 메서드 방식이 조금 더 낫다고 할 수 있다.
   가독성이 살짝 더 좋고, 잘못 사용했을 때 발견하기가 쉽다.
   상태검사메서드를 깜빡했다면 상태 의존적 메서드가 예외를 던진다.
   반면 특정 값은 검사하지 않고 지나쳐도 발견하기 어렵다 (옵셔널은 그렇지 않음)









## 정리

예외는 예외 상황에서 쓸 의도로 설계 되었다.

정상적인 제어프름에서는 사용하지 말고 그런 API 도 만들지 마라