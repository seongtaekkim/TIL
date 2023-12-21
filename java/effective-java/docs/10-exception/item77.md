# item77 예외를 무시하지 마라



### 개요

- 아래와 같이 예외를 무시하는 경우가 있다. 그러지좀마라

~~~java
try { ... } catch (SomeException e) {}
~~~



### 그럼에도 무시해야 할 예외도 있다. 

- FileInputStream 같은 경우 입력 전용 스트림으로 파일의 상태를 변경하지 않기에 복구 할 것도 없고, 스트림을 닫는다는 의미는 필요한 정보를 다 읽었다는 의미이기에 남은 작업을 중단하지 않아도 된다.
- 하지만, 동일한 예외가 반복된다면 조사가 필요할 수 있기에 catch 블록에 해당 사항에 대해 주석을 남기고 예외 변수의 이름도 ignored로 바꿔놓자. 

~~~java
Future<Integer> f = exec.submit(planarMap::chromaticNumber);
int numColors = 4; // 기본값. 어떤 지도라도 이 값이면 충분하다. 

try {
	numColors = f.get(1L, TimeUnit.SECONDS);
} catch (TimeoutException | ExecutionException ignored ) {
	 //기본 값을 사용한다. (색상 수를 최소화하면 좋지만, 필수는 아니다.)
}
~~~



### 정리

- checed, unchecked 예외 모두 동일하다. 예외를 무시하지 말자. 
- 바깥으로 전파되도록만 해도 디버깅 정보를 남기고 프로그램이 중단되어, 해결할 수 있다. 