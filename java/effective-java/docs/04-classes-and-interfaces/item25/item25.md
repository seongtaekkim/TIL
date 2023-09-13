# 톱레벨 클래스는 한 파일에 하나만 담으라

- 소스 파일 하나에 톱레벨 클래스를 여러 개 선언하더라도 자바 컴파일러는 불평하지 않는다.
- 하지만 아무런 득이 없을 뿐더러 심각한 위험을 감수해야 하는 행위다.
- 이렇게 하면 한 클래스를 여러가지로 정의할 수 있으며, 그 중 어느것을 사용할지는 어느 소스파일을 먼저 컴파일하냐에 따라 달라지기 때문이다.



### 아래 3개 파일이 있다.

- 중복된 top level 클래스가 존재할 때, 컴파일 시 발생하는 문제점에 대해 학습해보자

```java
// Main.java
public class Main {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
}
```

~~~java
// Utensil.java
class Utensil {
    static final String NAME = "pan";
}

class Dessert {
    static final String NAME = "cake";
}
~~~

~~~java
// Dessert.java
class Dessert {
    static final String NAME = "pie";
}
class Utensil {
    static final String NAME = "pot";
}
~~~





### 컴파일 순서,방법 별로 다른 결과 출력

- Utensil class 참조를 먼저 시도하면 Utencil.java 를 참조해서 컴파일하고
- 반대라면, Dessert.java 를 참조해서 컴파일 한다.

~~~java
public class Main {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
 				//System.out.println(Dessert.NAME + Utensil.NAME);
    }
}
~~~

~~~sh
$ javac Main.java
$ java Main

# result
pancake
~~~



- 명시를 해서 컴파일하면 생각한 대로 컴파일이 된다.

~~~sh
$ javac Main.java Utensil.java 
$ java Main
  
# result
pancake
~~~

~~~sh
$ javac Dessert.java Main.java
$ java Main

# result
potpip
~~~



### 굳이 같은 파일 안에 넣어야 한다면 정적맴버클래스(아이템 24) 처럼 해보면 좋다.

~~~java
public class Test {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }

    private static class Utensil {
        static final String NAME = "pan";
    }

    private static class Dessert {
        static final String NAME = "cake";
    }
}
~~~

~~~sh
$ javac Test.java
$ java Test

# result
pancake
~~~



## 정리

교훈은 명확하다. 소스파일 하나에는 반드시 톱레벨 클래스를 하나만 담자.
이 규칙만 따른다면 컴파일러가 한 클래스에 대한 정의를 여러 개 만들어내는 일은 사라진다.
소스파일을 어떤 순서로 컴파일하든 바이너리파일이나 프로그램의 동작이 달라지는 일은 결코 일어나지 않을 것이다.





















