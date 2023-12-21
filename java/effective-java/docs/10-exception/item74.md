# item74 메서드가 던지는 모든 예외를 문서화하라



메서드가 던지는 예외는 그 메서드를 올바로 사용하는 데 아주 중요한 정보다.

따라서 각 메서드가 던지느 예외 하나하나를 문서화 하는데 충분한 시간을 쏟아야 한다. (item56)



**checked exception은 항상 따로따로 선언하고, 각 예외가 발생하는 상황을 자바독의 @throws 태그를 사용하여 정확히 문서화 하자.**

- 공통상위 클래스 하나로 뭉뚱그리지 마라. (Exception, Throwable)
  - 해당 메서드 예외의 원인을 알기힘들어 API사용성을 떨어뜨린다.
- **유일한 예외는 main 메서드이다.** main은 오직 JVM만 호출하므로 Exception 을 던져도 된다.

~~~java
/**
 * @throws IllegalStateException
 */
public void testMethod(String parameter) throws IllegalStateException {
  
}
~~~





### unchecked exception  문서화

- 일반적으로 프로그래밍 오류를 뜻하는데, 문서화를 잘 해놓으면 개발자가 참고하기 좋다.
- public mehod 라면 필요 전제조건을 문서화 해야 한다 (item56)
  - 특히, 인터페이스 메서드에서 비검사 예외를 문서화하는 것이 중요하다. 문서화한 전제조건이 인터페이스의 일반 규약에 속하게 되어 그 인터페이스를 구현한 모든 구현체가 일관되게 동작하도록 해주기 때문이다.
- 현실적으로 unchecked exception  문서화 최신화가 어렵긴 하다. (해당 코드가 변경될 경우)



### 메서드가 던질 수 있는 예외를 각각 @throws 태그로 문서화하되, 비검사 예외는 메서드 선언의 throws 목록에 넣지 말자.

- 개발자 입장에서 두 가지를 확실히 구분하는 게 좋다.
- checked exception : @throws, throws
- unchecked exception : throws

~~~java
class Dummy {


  /**
   * @throws IOException - bla bla
   */
   public void methodA(String param) throws IOException {
       ...
   }

  /**
   * @throws RuntimeException
   */
   public void methodB(String param) {
       ...
   }

   public void methodC(String param) {
       ...
   }
}
~~~







한 클래스에 정의된 많은 메서드가 같은 이유로 같은 예외를 던진다면 그 예외를 클래스 설명에 추가하는 방법도 있다.

- 예) NullPointerException 

~~~java
/**
 * @throws NullPointerException - 모든 메서드는 param가 null이면 NullPointerExcetpion.
 */
class Dummy throws NullPointerException {

   public void methodA(String param) {
       ...
   }

   public void methodB(String param) {
       ...
   }

   public void methodC(String param) {
       ...
   }
}
~~~





## 정리

메서드가 던질 가능성이 있는 모든 예외를 문서화하라.
검사 예외든 비검사예외든, 추상메서드든 구체 메서드든 모두 마찬가지다.
문서화에는 자바독의 @throws 태그를 사용하면 된다.

검사예외만 메서드 선언의 throws문에 일일이 선언ㅇ하고, 비검사 예외는 메서드 선언에는 기입하지 말자.
발생 가능한 예외를 문서로 남기지 않으면 다른사람이 그 클래스나 인터페이스를 효과적으로 사용하기 어렵거나 심지어 불가능할 수도 있다.