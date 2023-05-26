## java code manipulation (바이트코드 조작)



## 바이트코드 조작

### 코드 커버리지 측정



### 모자에서 토끼를 꺼내는 마술



- bytecode를 조작하는 라이브러리 의존성 추가.

~~~xml
<!-- https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy/1.10.22 -->
<dependency>
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy</artifactId>
    <version>1.10.22</version>
</dependency>
~~~

- 코드조작 대상이다. 현재는 null값을 리턴하지만, 조작을 통해 문자열을 리턴할 것이다.

```
public class Moja {
    public String pullOut() {
        return "";
    }
}
```

- step1 : `Moja.class`를 읽어와서 method내용을 바꾸고 maven install 을 진행한다.
- step2 : 코드조작 부분을 없애고 함수만 출력로직을 넣어 실행하면 조작했던 부분이 출력된다.
  - target 의 jar 파일에는 조작한 클래스가 남아있기 때문에 가능한 방법이다.
- 두번에 나누어 테스트해야 하는 이유는, 코드를 조작하는 로직에서는 이미 class loader에 해당 클래스가 담겼기 때문에 활용할 수가 없다.

```
public class Masulsa {

    public static void main(String[] args) {
        try {
            new ByteBuddy().redefine(Moja.class)
                    .method(named("pullOut")).intercept(FixedValue.value("Rabbit!!!!!"))
                    .make().saveIn(new File("/Users/staek/Documents/java-projects/BytebuddyTest/bytebuddytest/target/classes/"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(new Moja().pullOut());
    }
}
```



### javaagent 실습

- `Moja.class`를 미리 지정하지 않고 classloader 에서 대상 클래스를 읽어서 method 의 정보를 직접 변경할 수도 있다.
- 하지만 해당 방법은 다른 클래스에서도 사용할 수 있기에 바람직하지 않은 방법이다.

```java
public class Masulsa {

    public static void main(String[] args) {

        ClassLoader classLoader = Masulsa.class.getClassLoader();
        TypePool typePool = TypePool.Default.of(classLoader);
        try {
            new ByteBuddy().redefine(
                    typePool.describe("Moja").resolve(), ClassFileLocator.ForClassLoader.of(classLoader))
                    .method(named("pullOut")).intercept(FixedValue.value("Rabbit!!!!!"))
                    .make().saveIn(new File("/Users/staek/Documents/java-projects/BytebuddyTest/bytebuddytest/target/classes/"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(new Moja().pullOut());
    }
}
```



- premain이라는 함수를 사용하면, 다른 프로젝트에서 현재 프로젝에서 만들어진 class 파일을 읽어서 사용할 수 있도록 유도할 수 있다.

```java
public class MasulsaAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default()
                .type((ElementMatchers.any()))
                .transform(((builder, typeDescription, classLoader, javaModule)
                        -> builder.method(named("pullOut")).intercept(FixedValue.value("Rabit by agent!!!")))).installOn(inst);
    }
}
```



- maven jar plugin manifest 검색후
- [custom menifest](https://maven.apache.org/plugins/maven-jar-plugin/examples/manifest-customization.html) 을 참고해서 아래와 같이 pom에 추가한다.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.3.0</version>
            <configuration>
                <archive>
                    <index>true</index>
                    <manifest>
                        <addClasspath>true</addClasspath>
                    </manifest>
                    <manifestEntries>
                        <mode>development</mode>
                        <url>${project.url}</url>
                        <key>value</key>
                        <Premain-Class>me.staek.MasulsaAgent</Premain-Class>
                        <Can-Redefine-Classes>true</Can-Redefine-Classes>
                        <Can-Restransform-Classes>true</Can-Restransform-Classes>
                    </manifestEntries>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```



- target jar 의 META_INF/MANIFEST.MF
  - manifest 에 추가한 옵션이 파일에 최종 추가됨을 확인.

~~~
Manifest-Version: 1.0
Created-By: Maven JAR Plugin 3.3.0
Build-Jdk-Spec: 11
Class-Path: byte-buddy-1.10.22.jar
Can-Redefine-Classes: true
Can-Restransform-Classes: true
Premain-Class: me.staek.MasulsaAgent
key: value
mode: development
url: 

~~~



- vm option 에 `-javaagent`옵션으로 미리 만들어 둔 MasulsaAgent프로젝트의 jar 파일 위치를 삽입하면 
  해당 .class에 입력된 값을 참조하여 결과를 보여주게 된다.

~~~java
//-javaagent:/Users/staek/Documents/java-projects/MasulsaAgent/MasulsaAgent/target/MasulsaAgent-1.0-SNAPSHOT.jar
public class Masulsa {
    public static void main(String[] args) {
        System.out.println(new Moja().pullOut());
    }
}
~~~









### reference

[백기선 인프런 강의](https://www.inflearn.com/course/the-java-code-manipulation/dashboard)

[인프런 학습자료](https://docs.google.com/document/d/11zgALhqn3igwfs4xc9cYdRPlyS84M6ba_6xz0I2M-Ik/edit#)





