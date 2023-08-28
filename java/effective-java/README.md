# Effective Java

- 2023년 스터디 내용 정리.
- 기간: 23.08.12 ~ 진행중



## Section

- [02-creating-and-destroying-objects](docs/02-creating-and-destroying-objects)
  - [item01](docs/02-creating-and-destroying-objects/item01) 정적팩터리메서드 |  service-provider, reflaction | 인터페이스와 정적메서드
  - [item02](docs/02-creating-and-destroying-objects/item02) Builder |  immutable-string
  - [item03](docs/02-creating-and-destroying-objects/item03) 싱글턴 | Serialization | method-reference
  - [item04](docs/02-creating-and-destroying-objects/item04) private 생성자 | AssertionError
  - [item05](docs/02-creating-and-destroying-objects/item05) 의존객체주입 |  Supplier | Factory-Method
  - [item06](docs/02-creating-and-destroying-objects/item06) 불필요한 객체생성 | Depracation
  - [item07](docs/02-creating-and-destroying-objects/item07) 리소스해제 | GC, Reference | Executor
  - [item08](docs/02-creating-and-destroying-objects/item08) Finalizer | Cleaner | AutoCloseable
  - [item09](docs/02-creating-and-destroying-objects/item09) try-with-resources | Throwable suppress
- [03-methods-common-to-all-objects](docs/03-methods-common-to-all-objects)
  - [item10](docs/03-methods-common-to-all-objects/item10) equals 재정의 | value-based-class | stackoverflow | 리스코프원칙 | Atomic
  - [item11](docs/03-methods-common-to-all-objects/item11) 해시코드 재정의 | HashMap | Thread safe | ConcurrentModificationException
  - [item12](docs/03-methods-common-to-all-objects/item12) toString 재정의
  - [item13](docs/03-methods-common-to-all-objects/item13) clone 재정의 | UncheckedException | TreeSet
  - [item14](docs/03-methods-common-to-all-objects/item14) Comparable | 제네릭과 Comparable | Comparable과 Comparator
- [04-classes-and-interfaces](docs/04-classes-and-interfaces)
  - [item15](docs/04-classes-and-interfaces/item15)  클래스맴버 접근권한 최소화 | java9 module
- [05-generics](docs/05-generics)
- [06-enums-and-annotations](docs/06-enums-and-annotations)
- [07-lambdas-and-streams](docs/07-lambdas-and-streams)
- [08-methods](docs/08-methods)
- [09-general-programming](docs/09-general-programming)
- [10-exceptions](docs/10-exceptions)
- [11-concurrency](docs/11-concurrency)
- [12-serialization](docs/12-serialization)

## [study issue](./99-issue)

- [volatile]() volatile 개념 및 테스트
- catched-exception
- real-memory-reference
- AssertionError 동작순서
- stack-pop GC test
- AutoCloseable - close() 호출위치
- Reference And Concurrency









## Reference

- [book](https://www.yes24.com/Product/Goods/65551284)
- [source](https://github.com/jbloch/effective-java-3e-source-code)
- [inflearn1-1](https://github.com/whiteship/effective-java)
- [inflearn1-2](https://github.com/whiteship/chinese-hello-service)
- spring framework API
- java API
