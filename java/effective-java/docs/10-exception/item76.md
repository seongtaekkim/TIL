# item76 가능한 한 실패 원자적으로 만들라



### 실패 원자적 (failure-atomic) ?

- 호출된 메서드가 실패하더라도 해당 객체는 메서드 호출 전 상태를 유지하도록 하는 특성을 실패 원자적(failure-atomic)이라 한다
- db의 rollback과 비슷



## 예시

### 1. 불변 객체 (item17)

- 생성시점 이후 리소스가 변경될 수 없어서 절대 변하지 않는다. 때문에 불변객체 자체로 실패원자적 이다.

### 2. 가변 객체는?

- 작업 수행에 앞서 매개변수 유효성을 검사한다. (item49)

~~~java
// resource 사용 전 크기를 미리 검사한다.
public Object pop() {
	if(size == 0) throw new EmptyStackException(); // 추상화된 Exception throw (item73)
	
	Object result = elements[--size]; // ArrayIndexOutOfBoundsException
	elements[size] = null; //다 쓴 참조 해제
	return result;
}
~~~



### 3. 실패할 수 있는 코드를 객체의 상태를 바꾸기전에 수행하자. 

- 객체의 상태가 변한 다음 로직이 실패하면 이미 늦기에 상태를 변경하기 전에 실패할 수 있는 코드들을 배치하는 방법으로, 계산 전에는 인수의 유효성을 검사할 수 없는 경우 2번 방식과 같이 사용할 수 있는 기법이다. 
- ex) TreeMap
  - 데이터 삽입 시 정렬직전 Comparator에서 타입을 체크하여 fail-fast가 가능하여 실패원자성을 획득할 수 있다.

~~~java
public class FailureAtomic_TreeMap {
    public static void main(String[] args) {
        TreeMap<Integer, Integer> frequencyTable = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        Integer[] arr = {10, 100, 50};
        for (Integer s : arr)
            frequencyTable.put(s, 1);

        Object o = "wrong data";
        try {
            frequencyTable.put((Integer) o, 1);
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(frequencyTable);
    }
}
~~~



### 4. 객체의 임시 복사본에서 작업을 수행하고 원 객체와 교체하는 방식

- 로직수행을 resource 복사본으로 진행 후 성공하면 다시 붙여넣는다. 
- Ex) CopyOnWriteArrayList

~~~java
// CopyOnWriteArrayList 의 sort는 복사본으로 진행.
public void sort(Comparator<? super E> c) {
    synchronized (lock) {
        sortRange(c, 0, getArray().length);
    }
}

@SuppressWarnings("unchecked")
void sortRange(Comparator<? super E> c, int i, int end) {
    // assert Thread.holdsLock(lock);
    final Object[] es = getArray().clone();
    Arrays.sort(es, i, end, (Comparator<Object>)c);
   setArray(es);
}
~~~



### 5. 실패를 가로채는 복구 코드

작업 도중 발생하는 실패를 가로채 복구 코드를 작성하여 작업 전 상태로 되돌리는 방법으로, 자주 사용되는 방법은 아니다. 
주로 디스크 기반의 내구성(durability)을 보장해야 하는 자료구조에서 쓰인다. 

- **사례가 뭐가 있을까..** DB롤백?



### 항상 실패 원자성을 달성할수는 없다. 

- 멀티 스레드 환경에서 객체를 동시에 수정하려 하면 객체의 일관성이 깨질 수 있는데(ConcurrentModificationException) 이런 예외를 잡아냈다고 해서 쓸 수 있는 상태라 하긴 힘들다. 
- Error는 복구할 수조차 없기에 AssertionError에 대해서는 실패 원자적으로 만들 필요가 없다. 
- 실패 원자적으로 만들기 위한 비용 및 복잡도가 아주 큰 경우 트레이드 오프를 계산해 결정할 필요가 있다. 다만, 이런 경우 실패할 경우 객체의 상태를 API 설명에 명시해야 한다. 







