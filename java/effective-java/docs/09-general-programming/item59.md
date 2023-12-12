# item59 라이브러리를 익히고 사용하라



- 랜덤 함수를 만들고 싶다.

~~~java
public class RandomBug {
    static Random rnd = new Random();

    static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }

    public static void main(String[] args) {
        int n = 2 * (Integer.MAX_VALUE / 3);
        int low = 0;
        for (int i = 0; i < 1000000; i++)
            if (random(n) < n/2)
                low++;
        System.out.println(low);
    }
}
~~~

위 코드는 아래 세가지 문제가 있다.

- n이 크지 않다면 반복될 것이다.

- 숫자가 고르게 분산되어 출력되지 않는다.

- 지정한 범위 바깥 수가 나올 수 있다.

  ~~~java
  System.out.println(Integer.MIN_VALUE); // -2147483648
  System.out.println(Math.abs(Integer.MIN_VALUE)); // -2147483648 * -1 = -2147483648
  System.out.println(Math.abs(Integer.MIN_VALUE) % 10000); // 음수
  ~~~

해결하기 위해서는 관련한 깊은 전문지식이 필요할 것이다. 이를 쉽게 해결할 방법이 있으니, 표준라이브러리를 사용하는 것이다.



## 표준라이브러리 이점

### 1. 전문가 경험을 활용

- 표준라이브러리를 사용하면 그 코드를 작성한 전문가의 지식과 여러분보다 앞서 사용한 다른 프로그래머들의 경험을 활용할 수 있다.

### 2. 시간 단축

- 내가 개발할 로직 외 부분은 상당부분 라이브러리가 이미 제공한다.

### 3. 성능 개선

- 사용자가 많고, 업계표준 벤치마크를 사용해 성능을 확인하기 때문에
  표준라이브러리 개발자들은 계속 노력한다.

### 4. 기능이 많아진다

- 부족한부분 발견 -> 개발자 커뮤니티 -> 논의 -> 다음 릴리즈 추가

### 5. 좋은코드

- 내가 작성한 코드가 많은사람한테 낯익은 코드가 된다.
- 자연스레 유지보수하기 좋아진다.





- 메이저 릴리즈마다 주목할 만한 수많은 기능이 라이브러리에 추가된다.

```java
// java9 에 추가됨
// 요청 url의 응답 출력 
try (InputStream in = new URL(args[0]).openStream()) {
    in.transferTo(System.out);
}
```





### 무슨라이브러리 공부함?

- 적어도 java.lang, java.util, java.io
- 컬렉션 프레임워크
- 스트림 라이브러리
- java.utili.concurrent



### 라이브러리에 원하는게 없다면

- 전문적인 기능을 요구할수록 그럴 수 있다.
- 서드파티 (구글 등) 에서 찾아보자
- 없다면 만들자!



## 정리

라이브러리를 사용하자. 있는지 모르겠으면 찾아보자. 