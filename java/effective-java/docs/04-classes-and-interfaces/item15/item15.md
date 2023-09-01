# Item 15. Minimize the accessibility of classes and members



### 정보은닉의 장점

- 시스템 개발 속도를 높인다. 여러 컴포넌트를 병렬로 개발할 수 있기 때문이다.
  - 인터페이스를 접점으로 개발.

- 시스템 관리비용을 낮춘다. 각 컴포넌트를 더 빨리 파악하여 디버깅할 수 있고, 다른 컴포넌트로 교체하는 부담도 적기 때문이다.
  - 인터페이스 기준으로 분석을 빠르게 할 수 있다.

- 정보은닉 자체가 성능을 높여주진 않지만, 성능 최적화에 도움을 준다.
  완성된 시스템을 프로파일링해 최적화할 컴포넌트를 정한다음 (아이템 67), 다른 컴포넌트에 영향을 주지 않고 해당 컴포넌트만 최적화 할 수 있기 때문이다.
- 소프트웨어 재사용성을 높인다. 외부에 거의 의존하지 않고 동작할 수 있는 컴포넌트라면 그 컴포넌트와 함께 개발되지 않은 낮선 환경에서도 유용하게 쓰일 가능성이 크기 때문이다.
- 큰 시스템을 제작하는 난이도를 낮춰준다.
  시스템 전체가 아직 완성되지 않은 상태여도 개별 컴포넌트의 동작을 검증할 수 있기 때문이다.
  - divide and conquer 방식. 






## 클래스와 인터페이스의 접근 제한자 사용 원칙

- 모든 클래스와 멤버의 접근성을 가능한 한 좁혀야 한다.
- 톱레벨 클래스와 인터페이스에 package-private 또는 public을 쓸 수 있다.
  - public으로 선언하면 API가 되므로 하위 호환성을 유지하려면 영원히 관리해야 한다.
    - 버전이 올라가면 package-private 로 바뀐다면,, 참조한 모든 클래스를 다 수정해야 한다.
  - 패키지 외부에서 쓰지 않을 클래스나 인터페이스라면 package-private으로 선언한다.
- 한 클래스에서만 사용하는 package-private 클래스나 인터페이스는 해당 클래 스에 private static으로 중첩 시키자. (아이템 24)





### 모든 클래스와 멤버의 접근성을 가능한 한 좁혀야 한다.** (항상 가장 낮은 접근수준을 부여해야 한다.)

- MemberService 인터페이스는 외부에서 사용할 의도로 만들어질테니 public 이 옳다.
- Member 같은 value class도 외부에서 사용할 테니 public 이 옳다.
- DefaultMemberService 같은 concrete class 는 외부에서 알지 않아도 MemberService 타입이므로 알아야 할 이유가 없어 package-private 을 고려하는게 좋다.

~~~java
public interface MemberService {
}

public class Member {
}

class DefaultMemberService implements MemberService {
...
}
~~~



### 한 클래스에서만 사용하는 package-private 톱레벨 클래스나 인터페이스는 이를 사용하는 클래스 안에 private static 으로 중첩시켜 보자. (아이템 24).

~~~java
public class PrivateStaticClass {
}
~~~

### move refactoring

- PrivateStaticClass 을 move refactoring 하면 아래와 같이 작성된다.
- static class인 이유: PrivateStaticClass가 외부에 있었을 때는 분명 DefaultMemberService 인스턴스가 생성된 후 사용했을 것이다. 그러므로, PrivateStaticClass는 DefaultMemberService에 종속적이지 않아야 논리적으로 옳다.

```java
class DefaultMemberService implements MemberService {
    
    public static class PrivateStaticClass {}
    ...
}
```

- 톱레벨로 두면 같은 패키지의 모든 클래스가 접근할 수 있지만, private static으로 중첩시키면 바깥 클래스 하나에서만 접근할 수 있다.





## 멤버(필드, 메서드, 중첩 클래스/인터페이스)의 접근 제한자 원칙

- private 멤버를 선언한 톱레벨 클래스에서만 접근가능하다.
- package-private 멤버가 소속된 패키지 안의 모든 클래스에서 접근할 수 있다.
  접근제한자를 명시하지 않았을 때 적용되는 패키지 접근수준이다. (단, 인터페이스의 멤버는 기본적으로 public.)
- protected: package-private 범위를 포함하고 이 멤버를 선언한 클래스의 하위 클래스에서도 접근할 수 있다.
- public: 모든 곳에서 접근가능.



### private -> package-private 고려

### 테스트 코드

- Mock 객체를 만든후, 특정로직을 거치고나서 필드등을 검사할 일이 있다.
- 이 때 필드는 대부분 private 일텐데, getter 가 이미 존재하면 사용하면 되지만 없을경우
- 해당필드를 package-private로 고려해 볼 수 있다. 가능여부를 따져보고 결정하면 될 것이다.
- 그렇다고 public 등으로 변경을 고려해서는 안된다.

~~~java
import me.whiteship.chapter04.item15.class_and_interfaces.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    MemberService memberService;

    MemberService memberServiceNull;

    @Test
    void itemService() {
        ItemService service = new ItemService(memberService);
        assertNotNull(service);
        assertNotNull(service.getMemberService());
    }

    @Test
    void itemService_null() {
        ItemService service = new ItemService(memberServiceNull);
        System.out.println(service);
    }
}
~~~

~~~java
public class ItemService {

    private MemberService memberService;
    ...
    public ItemService(MemberService memberService) {
        if (memberService == null) {
            throw new IllegalArgumentException("MemberService should not be null.");
        }
        this.memberService = memberService;
    }
    MemberService getMemberService() {
        return memberService;
    }
}
~~~







###  Serializable을 구현한 클래스에서는 그 필드들도 의도치 않게 공개 API가 될 수도 있다, (아이템 86, 87)

- 내부정보이지만 API가 될 수 있다.
- 직렬화 후에 public 으로 변경된다면, 역직렬화할 때(클래스정보가 바뀌어) 공개 API가 될 수 있다. 





### 리스코프 치환 원칙 위배

- 상위 클래스의 메서드를 재정의할때는 그 접근 수준을 상위 클래스에서보다 좁게 설정할 수 없다.
- 이 제약은 상위 클래스의 인스턴스는 하위 클래스의 인스턴스로 대체해 사용할 수 있어야 한다는 규칙 (리스코프 치환 원칙, 아이템10)을 지키기 위해 필요하다.
- 상위클래스 함수가 public 인데 하위클래스 함수가 private 으로 재정의 했다면 하위 타입 인스턴스로 바꾼 이후 해당 함수는 호출할 수 없게 된다. -> 리스코프 치환원칙 위배.



### public 클래스의 인스턴스 필드는 되도록 public이 아니어야 한다.(아이템 16)

- 필드가 가변객체를 참조하거나, final이 아닌 인스턴스 필드를 public으로 선언하면 그 필드에 담을 수 있는 값을 제한할 힘을 잃게 된다.
- 그 필드와 관련된 모든 것을 불변식을 보장할 수 없게 된다는 뜻이다.
- 여기에 더해, 필드가 수정될 때 (락 획득 같은) 다른 작업을 할 수 없게 되므로 **public 가변필드를 갖는 클래스는 일반적으로 스레드 안전하지 않다**.
  - **누구나 공유자원을 변경할 수 있기 때문**






### 기본타입이나 불변객체를 참조해야 한다 (아이템 17)

- 해당 클래스가 표현하는 추상개념을 완성하는 데 꼭 필요한 구성요소로써의 상수라면 public static final 필드로 공개해도 좋다.
- 관례상 이런 상수의 이름은 대문자 알파벳으로 쓰이며, 각단어 사이에 밑줄을 넣는다 (아이템 68)

~~~ java
public static final int DATA = 1;
public static final String[] LIST = new String[10]; # 내부 리소스는 변경 가능하므로 작성하면 안된다.
                    																# 보안적으로 위험
~~~

- 가변 객체를 참조한다면 final이 아닌 필드에 적용되는 모든 불이익이 그대로 적용된다.





## 자바9 모듈 (Java Platform Module System)



- [JSR-376](https://openjdk.org/projects/jigsaw/spec/) 스팩으로 정의한 자바의 모듈 시스템
- 안정성 - 순환 참조 허용하지 않음, 실행시 필요한 모듈 확인, 한 패키지는 한 모듈 에서만 공개할 수 있음.
- 캡슐화 - public 인터페이스나 클래스라 하더라도, 공개된 패키지만 사용할 수 있 다. 내부 구현을 보호하는 수단으로 사용할 수 있다.
  - protected 혹은 public 멤버라도 해당 패키지를 공개하지 않았다면 모듈 외부에서는 접근할 수 없다. 
  - 하지만 모듈이 아닌 곳에서 참조가 가능하다는 문제가 있다.
- 확장성 - **필요한 자바 플랫폼 모듈만 모아서 최적의 JRE를 구성할 수 있다. 작은 기기에서 구동할 애플리케이션을 개발할 때 유용하다.**
- [모듈테스트해보기](./item15-java-module.md)



### 장점

- 실행할 때 이미 module 유무를 파악후 알게된다. (원래 앱은 런타임에 알게 됨)

### 안쓰는 이유

여러분 모듈의 JAR 파일을 자신의 모듈 경로가 아닌 애플리케이션의 classpath에 두면 그 모듈 안의 모든 패키지는 마치 모듈이 없는것처럼 행동한다.
즉, 모듈이 공개했는지 여부와 상관없이, public 클래스가 선언한 모든 public 혹은 protected 멤버를 모듈 밖에서도 접근할 수 있다.



### 결과

**일반 패키지로의 모든 접근에 특벌한 조치를 취해야 한다.**
**JDK 외에도 모듈 개념이 널리 받아들여질지 예측하기는 아직 이른 감이 있다.**







## 정리

프로그램 요소의 접근성은 가능한 한 최소한으로 하라. 꼭 필요한 것만 골라 최소한의 public API를 설계하자.
그 외에는 클래스, 인터페이스, 멤버가 의도치 않게 API 공개되는 일이 없도록 해야한다.
public 클래스는 상수용 public static final 필드 외에는 **어떠한 public필드도 가져서는 안된다.**
public static final 필드가 참조하는 객체가 불변인지 확인해라.







































