# Effective Java

- 2023년 스터디 내용 정리.
- 기간: 23.08.12 ~ 진행중



## Section

- [02-creating-and-destroying-objects](docs/02-creating-and-destroying-objects)
  - [item01](docs/02-creating-and-destroying-objects/item01) 정적팩터리메서드 |  service-provider | reflaction | 인터페이스와 정적메서드
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
  - [item11](docs/03-methods-common-to-all-objects/item11) 해시코드 재정의 | HashMap | Thread safe | Concurrent Read And Write
  - [item12](docs/03-methods-common-to-all-objects/item12) toString 재정의
  - [item13](docs/03-methods-common-to-all-objects/item13) clone 재정의 | UncheckedException | TreeSet
  - [item14](docs/03-methods-common-to-all-objects/item14) Comparable | 제네릭과 Comparable | Comparable과 Comparator
- [04-classes-and-interfaces](docs/04-classes-and-interfaces)
  - [item15](docs/04-classes-and-interfaces/item15)  클래스맴버 접근권한 최소화 | java9 module
  - [item16](docs/04-classes-and-interfaces/item16)  public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라. | Dimension copy 문제
  - [item17](docs/04-classes-and-interfaces/item17) 변경 가능성을 최소화 하라 | 자바 메모리 모델 | java.util.concurrent | CountDownLatch
  - [item18](docs/04-classes-and-interfaces/item18) 상속보다는 컴포지션(위임) | 데코레이터 패턴 | 콜백 프레임워크
  - [item19](docs/04-classes-and-interfaces/item19) 상속을 고려한 설계 | 문서화
  - [item20](docs/04-classes-and-interfaces/item20) 추상클래스보다는 인터페이스 | 템플릿 메서드 패턴
  - [item21](docs/04-classes-and-interfaces/item21) 인터페이스와 구현 | ConcurrentModificationException
  - [item22](docs/04-classes-and-interfaces/item22) 인터페이스와 타입정의
  - [item23](docs/04-classes-and-interfaces/item23) 태그달린 클래스와 클래스 계층구조
  - [item24](docs/04-classes-and-interfaces/item24) 맴버클래스와 static | 어댑터
  - [item25](docs/04-classes-and-interfaces/item25) 톱레벨 클래스 
- [05-generics](docs/05-generics)
  - [item26](docs/05-generics/item26) raw type 사용하지마 | GenericRepository
  - [item27](docs/05-generics/item27) 비검사 경고 제거
  - [item28](docs/05-generics/item28) 배열보다는 리스트 | @SafeVarags
  - [item29](docs/05-generics/item29) 이왕이면 제네릭타입 | 한정적 타입 매개변수
  - [item30](docs/05-generics/item30) 이왕이면 제네릭메서드 | 재귀적 타입 한정

- [06-enums-and-annotations](docs/06-enums-and-annotations)
- [07-lambdas-and-streams](docs/07-lambdas-and-streams)
- [08-methods](docs/08-methods)
- [09-general-programming](docs/09-general-programming)
- [10-exceptions](docs/10-exceptions)
- [11-concurrency](docs/11-concurrency)
- [12-serialization](docs/12-serialization)

## [study issue](./99-issue)

- [volatile](./docs/99-issue/volatile.md) volatile 개념 및 테스트
- [catched-exception](./docs/99-issue/checked-exception-performance.md)
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
