

## Functional Interface

- 함수형 인터페이스는 람다 표현식과 메소드 참조에 대한 타겟 타입을 제공한다. 
- 타겟 타입은 변수 할당, 메소드 호출, 타입 변환에 활용할 수 있다. 





~~~java
// 인자1 input 안자2 output
Function<Integer, String> intToString = Object::toString;

// 인자1 output
Supplier<Person> personSupplier = Person::new;

// 인자1 input 인자2 output
Function<LocalDate, Person> personFunction = Person::new;

// 인자1 input
Consumer<Integer> integerConsumer = System.out::println;

// 인자1 input boolean return
Predicate<Person> predicate;
~~~



~~~java
@FunctionalInterface
public interface MyFunction {

    String valueOf(Integer integer);
		void a();
    static String hello() {
        return "hello";
    }
}
~~~

- @FunctionalInterface 는 함수 하나만 선언해야 한다.
- 두개를 선언하면 컴파일 에러가 발생하는데, 해당 로직은 [annotation processor](../../../../bytecode/annotation-processor.md) 에서 처리한다





