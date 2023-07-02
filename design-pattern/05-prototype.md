

## 1. intro

![스크린샷 2023-07-02 오전 10.49.51](img/prototype-01.png)

기존 인스턴스를 복제하여 새로운 인스턴스를 만드는 방법

- 복제 기능을 갖추고 있는 기존 인스턴스를 프로토타입으로 사용해 새 인스턴스를 만들 수 있다.







## 2. implement

1. Clonable 을 implements 한다.
2. Object의 clone()을 override 한다.
   - default 는 shallow copy 이기 때문에, deep copy 로 override 할 수 있다.
     - shallow copy 는 타겟객체 내부에 다른 객체 레퍼런스를 복사한다는 점에 유의해야 한다.
3. Object의 eqauls(), hashcode() 를 override한다.
   1. clone() 을 deep copy한다면,  shallow copy 를 비교했던 equals도 override 하는게 논리상 맞다.
   2. equals() override 할 때, hashcode() 도 해야하는데, 이유는 이펙티브자바에서 확인하자

```java
public class GithubIssue implements Cloneable {

 ...

    @Override
    protected Object clone() throws CloneNotSupportedException {
        GithubRepository repository = new GithubRepository();
        repository.setUser(this.repository.getUser());
        repository.setName(this.repository.getName());

        GithubIssue githubIssue = new GithubIssue(repository);
        githubIssue.setId(this.id);
        githubIssue.setTitle(this.title);

        return githubIssue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubIssue that = (GithubIssue) o;
        return id == that.id && Objects.equals(title, that.title) && Objects.equals(repository, that.repository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, repository);
    }
...
}
public static void main(String[] args) throws CloneNotSupportedException {
        GithubRepository repository = new GithubRepository();
        repository.setUser("whiteship");
        repository.setName("live-study");

        GithubIssue githubIssue = new GithubIssue(repository);
        githubIssue.setId(1);
        githubIssue.setTitle("1주차 과제: JVM은 무엇이며 자바 코드는 어떻게 실행하는 것인가.");

        String url = githubIssue.getUrl();
        System.out.println(url);

        GithubIssue clone = (GithubIssue) githubIssue.clone();
        System.out.println(clone.getUrl());

        repository.setUser("Keesun");

        System.out.println(clone != githubIssue);
        System.out.println(clone.equals(githubIssue));
        System.out.println(clone.getClass() == githubIssue.getClass());
        System.out.println(clone.getRepository() == githubIssue.getRepository());

        System.out.println(clone.getUrl());
    }
```



## 3. Strength and Weakness

### 장점

- 복잡한 객체를 만드는 과정을 숨길 수 있다
- 기존 객체를 복제하는 과정이 새 인스턴스를 만드는 것보다비용(시간 또는 메모리) 적인 면에서 효율적일 수 있다.
- 추상적인 타입을 리턴할 수 있다.

### 단점

- 복제한 객체를 만드는 과정 자체가 복잡할 수 있다 (특히, 순환참조 있는경우)



## 4. API example

### List & ArrayList

- 보통은 추상적인 타입(list)으로 사용하는데, List 에서는 clonable 을 상속받지 않는다
  - ArrayList는 cloneabe 상속 받음
- 그래서 List는 대안으로 아래와 같이 shallow copy 한다.

```java
public class JavaCollectionExample {

    public static void main(String[] args) {
        Student keesun = new Student("keesun");
        Student whiteship = new Student("whiteship");
        List<Student> students = new ArrayList<>();
        students.add(keesun);
        students.add(whiteship);

        List<Student> clone = new ArrayList<>(students);
        System.out.println(clone);
    }
}
```

### ModelMapper

- modelmapper는 타겟 class의 private 변수들을 리플렉션으로 접근해서 복사하는 방법을 사용한다.
  - 자세한 내용은 아래 url에서 참조가 가능하다.

```java
public class ModelMapperExample {

    public static void main(String[] args) {
        GithubRepository repository = new GithubRepository();
        repository.setUser("whiteship");
        repository.setName("live-study");

        GithubIssue githubIssue = new GithubIssue(repository);
        githubIssue.setId(1);
        githubIssue.setTitle("1주차 과제: JVM은 무엇이며 자바 코드는 어떻게 실행하는 것인가.");

        // 리플렉션으로 알아내서 복사해서 넣어줌
        ModelMapper modelMapper = new ModelMapper();
        GithubIssueData githubIssueData = modelMapper.map(githubIssue, GithubIssueData.class);
        System.out.println(githubIssueData);
    }
}
```

[ModelMapper - Simple, Intelligent, Object Mapping.](http://modelmapper.org/)