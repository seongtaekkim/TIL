# checked exception performance

- checked exception 을 자주 사용하면 성능에 안좋다고 한다.
- 안좋은 이유와 대안 등을 알아보자.



## Exception 성능개선

- Exception 발생 후  Throwable 이 생성될 때 fillInStackTrace 메서드가 호출된다.
- 이 메서드는 비용이 꽤 커 보인다. (call stack을 순회하면서 클래스명, 메서드명, 코드번호 등 을 만들기 때문이다)
- 굳이 이런 정보가 필요없다면 (로그를 따로 작성하는 경우) CustomException 을 만들어 fillInStackTrace를 생략 할 수 있다.

~~~java
public synchronized Throwable fillInStackTrace() {
    if (stackTrace != null ||
        backtrace != null /* Out of protocol state */ ) {
        fillInStackTrace(0);
        stackTrace = UNASSIGNED_STACK;
    }
    return this;
}
~~~



### CustemException

- 아래와 같이 fillInStackTrace 를 ovveride 하여 기능을 생략하면 비용을 크게 줄일 수 있다.
- 또한 정적팩터리메서드르 만들어 반복하는 throw new 에대한 비용도 줄일 수 있다.

~~~java
public class CustemException extends Exception {
    public static final CustemException CUSTOM_EXCEPTION = new CustemException();


    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
~~~





### Test

- me.staek.issue.checked_exception_performance 코드 참조 후 테스트

1. benchmark 테스트 (1)

   ~~~sh
   # throwExceptionAndUnwindStackTrace: depth 0개
   Benchmark                                                 Mode  Cnt   Score   Error  Units
   ExceptionBenchmark.createExceptionWithoutThrowingIt       avgt       11.208          ms/op
   ExceptionBenchmark.doNotThrowException                    avgt        0.024          ms/op
   ExceptionBenchmark.throwAndCatchException                 avgt       11.197          ms/op
   ExceptionBenchmark.throwExceptionAndUnwindStackTrace      avgt       55.108          ms/op
   ExceptionBenchmark.throwExceptionWithoutAddingStackTrace  avgt        0.397          ms/op
   ~~~

2. benchmark 테스트 (2)
   ~~~sh
   # throwExceptionAndUnwindStackTrace: depth 5개
   Benchmark                                                 Mode  Cnt   Score   Error  Units
   ExceptionBenchmark.createExceptionWithoutThrowingIt       avgt       11.665          ms/op
   ExceptionBenchmark.doNotThrowException                    avgt        0.024          ms/op
   ExceptionBenchmark.throwAndCatchException                 avgt       12.294          ms/op
   ExceptionBenchmark.throwExceptionAndUnwindStackTrace      avgt       61.933          ms/op
   ExceptionBenchmark.throwExceptionWithoutAddingStackTrace  avgt        0.404          ms/op
   ~~~

3. CheckedExceptionTimeTest.java : Exception (unwindstacktrace, non unwindstacktrace)
   ~~~sh
   time: 1994 ms (unwindstacktrace)
   time: 768 ms  (non unwindstacktrace)
   ~~~

4. CheckedExceptionTimeTest.java : CustomException (unwindstacktrace, non unwindstacktrace)
   ~~~sh
   time: 153 ms (unwindstacktrace)
   time: 61 ms  (non unwindstacktrace)
   ~~~

5. CheckedExceptionTimeTest.java : CustomException + 정적팩터리메서드 (unwindstacktrace, non unwindstacktrace)
   ~~~sh
   time: 74 ms (unwindstacktrace)
   time: 23 ms (non unwindstacktrace)
   ~~~

   



### reference

https://www.baeldung.com/java-exceptions-performance

https://github.com/eugenp/tutorials/tree/master/performance-tests

https://recordsoflife.tistory.com/1376

https://moonsiri.tistory.com/174





