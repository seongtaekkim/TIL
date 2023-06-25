# Annotation Processor

- 선언한 애노테이션을 사용한 로직에 대해서 컴파일시점에 검증하거나, 코드(bytecode)를 추가할 수 있다.



### Annotation Processor 생성

~~~java
@Target(ElementType.TYPE) // Interface, Class, Enum에 적용됨
@Retention(RetentionPolicy.SOURCE)
public @interface ProcessorInterface {}
~~~



~~~java
// META-INF > services 에 추가하지 않고
// auto-service jar를 이용해서 쉽게 추가할 수 있다.
//@AutoService(Processor.class)
public class StaekProcessor extends AbstractProcessor {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ProcessorInterface.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(ProcessorInterface.class);
        for(Element element : elementsAnnotatedWith) {
            if (element.getKind() != ElementKind.INTERFACE) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "annotation can't be used on " + element.getSimpleName());
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "processing " + element.getSimpleName());
            }
        }
        return true;
    }
}
~~~



### java 에서 아래 두가지 방법으로 service를 loader할 수 있다.

### META-INF > services

![스크린샷 2023-08-08 오후 11.57.03](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-08 오후 11.57.03.png)



### @AutoService

- 서비스로더용 레지스트리파일을 만드다

~~~xml
  <!-- https://mvnrepository.com/artifact/com.google.auto.service/auto-service -->
  <dependency>
    <groupId>com.google.auto.service</groupId>
    <artifactId>auto-service</artifactId>
    <version>1.1.1</version>
  </dependency>
~~~



### Processor Test

~~~xml
  <groupId>me.staek</groupId>
  <artifactId>call-processor</artifactId>
  <version>1.0-SNAPSHOT</version>
~~~

~~~java
@ProcessorInterface
public interface interface1 {
    public String method1();
}
~~~



- 컴파일 에러 발생
  - Interface 가 아닌곳에 에노테이션을 작성했으므로, processingEnv 에 의해 컴파일 에러가 발생한다.

~~~java
@ProcessorInterface
public class class1 {
}
~~~

![스크린샷 2023-08-09 오전 12.02.38](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-09 오전 12.02.38.png)



### 동작원리



**** 찾아서 학습해야 함 ***

- 동작원리
- AbstractProccsor
- procesingEnv
  - AbstractProccsor 상속클래스는 procesingEnv 사용 가능.
-  AutoService, Javapoet

- Typeelement, RoundEnvirenment





### API 예시

@FunctionalInterface

@Override

https://cyk0825.tistory.com/30





### 장단점

애노테이션 프로세서의 장점

- 컴파일 시점에 조작하기 때문에 런타임 비용이 없다.

애노테이션 프로세서의 단점

- 기존의 코드를 고치는 방법 -> 현재로써는 public 한 api가 없다고 한다.







