

# spring ioc



~~~java
@Component
public class SpellChecker {

    private Dictionary dictionary;
    ...
 }
 
@Component
public class SpringDictionary implements Dictionary {
...
}
~~~

- @Component 를 제외하고는 spring 코드가 없다.
- spring은 pojo(Plain Old Java Object) 를 지향한다.



~~~java
@Configuration
@ComponentScan(basePackageClasses = AppConfig.class)
public class AppConfig {
}
~~~



~~~java
public static void main(String[] args) {
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
    SpellChecker spellChecker = applicationContext.getBean(SpellChecker.class);
}
~~~

- configuration 에 정의 되어있는 @ComponantScan 에 의해 @Component 를 모두 스캔하여 bean을 생성한다.







** spring framework ioc 구조분석 학습 필요.

