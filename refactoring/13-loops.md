# 냄새 13. 반복문 (Loops)

- 프로그래밍 언어 초기부터 있었던 반복문은 처음엔 별다른 대안이 없어서 간과했지만 최근
Java와 같은 언어에서 함수형 프로그래밍을 지원하면서 반복문에 비해 더 나은 대안책이
생겼다.
- “반복문을 파이프라인으로 바꾸는 (Replace Loop with Pipeline)” 리팩토링을 적용하면
필터나 맵핑과 같은 파이프라인 기능을 사용해 보다 빠르게 어떤 작업을 하는지 파악할 수
있다.


## 리팩토링 33. 반복문을 파이프라인으로 바꾸기 (Replace Loop with Pipeline)

- 콜렉션 파이프라인 (자바의 Stream, C#의 LINQ - Language Integrated Query)
- 고전적인 반복문을 파이프라인 오퍼레이션을 사용해 표현하면 코드를 더 명확하게 만들 수있다.
    - 필터 (filter): 전달받은 조건의 true에 해당하는 데이터만 다음 오퍼레이션으로 전달.
    - 맵 (map): 전달받은 함수를 사용해 입력값을 원하는 출력값으로 변환하여 다음 오퍼레이션으로 전달.
- https://martinfowler.com/articles/refactoring-pipelines.html

### 변경 전

- 기존의 반복문은, 여러 조건이 있을 경우 한줄씩 분석하면서 봐야하는, 복잡한 면이있다.
- 요즈음은 함수형프로그래밍을 지원하여 파이프라인을 활용해 직관적으로 코드를 짤 수 있게 되었다.

```java
static public List<String> TwitterHandles(List<Author> authors, String company) {
        var result = new ArrayList<String> ();
        for (Author a : authors) {
            if (a.company.equals(company)) {
                var handle = a.twitterHandle;
                if (handle != null)
                    result.add(handle);
            }
        }
        return result;
	    }
```

### 변경 후

- java는 stream 을 활용하여 여러 기능을 사용할 수 있는데
if문은 filter로 대처하고, 반복문의 객체에서 어떤 다른 오브젝트를 꺼낼 때에는 map을 활용하고, collect 를 이용해서 다시 list로 합치는 등의 기능을 제공해준다.

```java
static public List<String> TwitterHandles(List<Author> authors, String company) {
        return authors.stream().filter(author -> author.company.equals(company))
                .map(author -> author.twitterHandle)
                .filter(a -> a != null)
                .collect(Collectors.toList());
    }
```

```java
@Test
    void twitterHandler() {
        Author keesun = new Author("ms", null);
        Author whiteship = new Author("naver", "whiteship");
        assertEquals(List.of("whiteship"), Author.TwitterHandles(List.of(keesun, whiteship), "naver"));
    }
```