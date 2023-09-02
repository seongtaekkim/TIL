# concurrent read and write



- 하나 이상의 스레드가 컬렉션을 반복하는 사이에 하나의 스레드가 데이터변경(리소스 추가, 삭제 또는 업데이트)하는 것을 Concurrent Modification이라 한다.
- Concurrent Modification 가능한 / 불가능한 자료구조에 대해 알아보자.



## HashMap

- 동기화를 지원하지 않는다.
- iterators는 **fail-fast** 라고 설명한다.
  - 문제가 발생하면 실행을 중단하고 최대한 빨리 예외를 던진다.

https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/HashMap.html

- *fail-fast*: if the map is structurally modified at any time after the iterator is created, in any way except through the iterator's own `remove` method, the iterator will throw a[`ConcurrentModificationException`](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/ConcurrentModificationException.html).

~~~java
HashMap<PhoneNumber, String> map = new HashMap<PhoneNumber, String>();
map.put(new PhoneNumber(1,1,1), "one");
map.put(new PhoneNumber(2,2,2), "two");

Iterator<PhoneNumber> it = map.keySet().iterator();

map.remove(new PhoneNumber(2,2,2));

while (it.hasNext()) {
    PhoneNumber key = it.next();
}
~~~





## HashTable

- 동기화를 지원한다. 동시성을 지원하지 않는다.
- iterator는 **fail-fast** 이다.

~~~java
Hashtable<PhoneNumber, String> table = new Hashtable<PhoneNumber, String>();
table.put(new PhoneNumber(1,1,1), "one");
table.put(new PhoneNumber(2,2,2), "two");

Iterator<PhoneNumber> it = table.keySet().iterator();

//        table.remove(new PhoneNumber(2,2,2));

while (it.hasNext()) {
    PhoneNumber key = it.next();
}
~~~

하지만 Enumeration 를 제공해주어 데이터를 조회할 수 있다.

~~~java
Hashtable<PhoneNumber, String> table2 = new Hashtable<PhoneNumber, String>();
table2.put(new PhoneNumber(1,1,1), "one");
table2.put(new PhoneNumber(2,2,2), "two");

Enumeration<PhoneNumber> enumKey = table2.keys();
table2.remove(new PhoneNumber(2,2,2));
while (enumKey.hasMoreElements()) {
    PhoneNumber key = enumKey.nextElement();
    System.out.println(key.getLineNum());
}
~~~







## CopyOnWriteArrayList

- 동시성을 지원한다.
- 쓰기작업, iterator 리턴에 대해 리소스를 copy 한다.
  - 쓰기할 때마다 copy 하기 때문에 속도가 느리다.
- copy를 읽기 때문에(iterator 포함) synchronization 이 없어 빠르고 여러 스레드 동시접근이 가능하다.
- iterator 는 copy 데이터기 때문에 중간에 리소스가 변경되어도 반영이 안된다. **(fail-safe)**
  - Fail-safe : 동작중에 오류가 발생해도 중지하지 않음.

~~~java
CopyOnWriteArrayList<PhoneNumber> list = new CopyOnWriteArrayList<PhoneNumber>();

list.add(new PhoneNumber(1,1,1));
list.add(new PhoneNumber(2,2,2));
list.add(new PhoneNumber(6,6,6));

Iterator<PhoneNumber> it4 = list.iterator();
list.remove(new PhoneNumber(2,2,2));

while (it4.hasNext()) {
    list.add(new PhoneNumber(8,8,8));
    PhoneNumber key = it4.next();
    System.out.println(key.getLineNum());
}
~~~







## ConcurrentHashMap

- 동시성을 지원한다.

- 쓰기: 버킷 추가 시 CAS, 버킷에 연결 시 synchronized 한다.

- 읽기: 여러 스레드 접근을 허용한다. **(weakly consistent iterator)**

  https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/concurrent/package-summary.html

  - Most concurrent Collection implementations (including most Queues) also differ from the usual `java.util` conventions in that their [Iterators](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/Iterator.html) and [Spliterators](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/Spliterator.html) provide *weakly consistent* rather than fast-fail traversal:
    - they may proceed concurrently with other operations
    - they will never throw [`ConcurrentModificationException`](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/ConcurrentModificationException.html)
    - they are guaranteed to traverse elements as they existed upon construction exactly once, and may (but are not guaranteed to) reflect any modifications subsequent to construction.

- **iterator 순회 중 데이터가 변경됐을 때, 이전순서라면 조회가 안되고, 나중 순서라면 조회가 된다.**

~~~java
ConcurrentHashMap<PhoneNumber, String> table3 = new ConcurrentHashMap<PhoneNumber, String>();
table3.put(new PhoneNumber(1,1,1), "one");
table3.put(new PhoneNumber(2,2,2), "two");
table3.put(new PhoneNumber(6,6,6), "six");

Iterator<PhoneNumber> it3 = table3.keySet().iterator();

table3.remove(new PhoneNumber(2,2,2));

while (it3.hasNext()) {
    PhoneNumber key = it3.next();
    table3.put(new PhoneNumber(4,4,4), "four");
    System.out.println(key.getLineNum());
}
~~~



































