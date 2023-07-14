## 1. intro

![스크린샷 2023-07-14 오전 8.38.36](img/strategy-01.png)

- 여러 알고리듬을 캡슐화하고 상호교환 가능하게 만드는 패턴

  - 컨텍스트에서 사용할 알고리듬을 클라이언트가 선택한다.

  



## 2. implement

### 변경 전

![스크린샷 2023-07-14 오전 8.39.37](img/strategy-02.png)

- 해당 기능은 BlueLightRedLight 생성자에 숫자를 넣어 생성하고, 숫자에 따라서 기능이 변경이 되는 형식의 프로그램이다.

```java
public class Client {

    public static void main(String[] args) {
        BlueLightRedLight blueLightRedLight = new BlueLightRedLight(3);
        blueLightRedLight.blueLight();
        blueLightRedLight.redLight();
    }
}
```

- speed 라는 변수를 설정하면, blueLite(), redLight() 기능이 분기처리를 통해 기능을 실행한다.
- speed가 추가되거나 변경되면, 모든 기능의 코드가 변경되어야 한다.

```java
public class BlueLightRedLight {

    private int speed;

    public BlueLightRedLight(int speed) {
        this.speed = speed;
    }

    public void blueLight() {
        if (speed == 1) {
            System.out.println("무 궁 화    꽃   이");
        } else if (speed == 2) {
            System.out.println("무궁화꽃이");
        } else {
            System.out.println("무광꼬치");
        }

    }

    public void redLight() {
        if (speed == 1) {
            System.out.println("피 었 습 니  다.");
        } else if (speed == 2) {
            System.out.println("피었습니다.");
        } else {
            System.out.println("피어씀다");
        }
    }
}
```

### 변경 후

![스크린샷 2023-07-14 오전 8.39.54](img/strategy-03.png)



- 컨텍스트 객체를 생성할 때, 구현된 전략객체를 인자로 넣어 런타임에서 관계가 형성되도록 client를 구현한다.
- 코드 시점에서 전략과 컨텍스트가 의존적이지 않다.

```java
public static void main(String[] args) {
        BlueLightRedLight game = new BlueLightRedLight();
        game.blueLight(new Normal());
        game.redLight(new Fastest());
        game.blueLight(new Speed() {
            @Override
            public void blueLight() {
                System.out.println("blue light");
            }

            @Override
            public void redLight() {
                System.out.println("red light");
            }
        });
    }
}
```

- BlueLightRedLight class는 기능이 정의된 Context 부분에 해당한다.
- Strategy인 Speed에 기능을 위임하여, 구현클래스 변경에 대해 영향을 받지 않는다.

```java
public class BlueLightRedLight {

    public void blueLight(Speed speed) {
        speed.blueLight();
    }

    public void redLight(Speed speed) {
        speed.redLight();
    }
}
```

- Strategy 에 해당하는 Speed 이고, 함수를 선언한다.

```java
public interface Speed {

    void blueLight();

    void redLight();

}
```

- Concrete Strategy 에 해당하여 실제 기능을 구현하게 된다.

```java
public class Fastest implements Speed{
    @Override
    public void blueLight() {
        System.out.println("무광꼬치");
    }

    @Override
    public void redLight() {
        System.out.println("피어씀다.");
    }
}
```



## 3. Strength and Weakness

여러 알고리듬을 캡슐화하고 상호교환 가능하게 만드는 패턴.

### 장점

- 새로운 전략을 추가하더라도 기존코드를 변경하지 않는다.
- 상속대신 위임을 사용할 수 있다.
- 런타임에 전략을 변경할 수 있다.

### 단점

- 복잡도가 증가한다.
- 클라이언트 코드가 구체적인 전략을 알아야 한다.



## 4. API example

### Comparator

```java
public class StrategyInJava {

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(5);

        System.out.println(numbers);

        Collections.sort(numbers, Comparator.naturalOrder());

        System.out.println(numbers);
    }
}
```

### Spring

- spring 에 상당부분이 전략패턴으로 이루어져 있다.

```java
public class StrategyInSpring {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        ApplicationContext applicationContext1 = new FileSystemXmlApplicationContext();
        ApplicationContext applicationContext2 = new AnnotationConfigApplicationContext();

        BeanDefinitionParser parser;

        PlatformTransactionManager platformTransactionManager;

        CacheManager cacheManager;
    }
}
```