# item54 null이 아닌 빈 컬렉션이나 배열을 반환하라



- 컨테이너의 자원 리턴 시 null인 경우 null, 그 외에는 방어적복사 인스턴스를 리턴하였다.

~~~java
private final List<Cheese> chesseInStock = new ArrayList<>();

public List<Cheese> getCheeses() {
    return chesseInStock.isEmpty() ? null : new ArrayList<>(chesseInStock);
}
~~~

- 클라이언트는 null 체크도 따로 해주어야 하는 리스크가 있다.

~~~java
Shop shop = new Shop();
List<Cheese> cheeses = shop.getCheeses();
if (cheeses != null && cheeses.contains(Cheese.STILTON))
    System.out.println("good");
~~~



### 성능이슈로 null 반환 ? -->> NO

- 빈 컨테이너 할당이 성능 저하 주범인지 확인되지 않으면 속단할 수 없다. (item67)
- 정팩메로 리턴하면 할당비용 없음.



빈 컬렉션 반환 (1) - 빈 컬렉션

~~~java
public List<Cheese> getCheeses() {
    return new ArrayList<>(chesseInStock);
}
~~~



빈 컬렉션 반환 (2) - 정팩메

- Collections 의 정팩메 활용 (emptyList, emptySet, emptyMap) 
  - 위 예제에서 성능이슈가 있을 경우 사용하자.

~~~java
public List<Cheese> getCheeses() {
    return chesseInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(chesseInStock);
}
~~~



빈 컬렉션 반환 (3) - 배열

- null 말고 0크기 배열 반환!

~~~java
public Cheese[] getCheeses() {
    return chesseInStock.toArray(new Cheese[0]);
}
~~~

- 배열도 정팩메 활용가능!

~~~java
private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];
public Cheese[] getCheeses() {
    return chesseInStock.toArray(EMPTY_CHEESE_ARRAY);
}
~~~





## 정리

null이 아닌, 빈 배열이나 컬렉션을 반환하라.

null반환 api는 사용하기 어렵고 오류처리 코드도 늘어난다. 성능이 좋지도 않다.