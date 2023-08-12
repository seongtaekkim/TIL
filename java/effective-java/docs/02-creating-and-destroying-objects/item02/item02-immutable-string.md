# Immutable String



- String을 literal로 생성할 경우, constant pool 에 문자열이 저장된다.
- 같은 문자열이 있다면 생성하지 않고 레퍼런스만 리턴한다.
- 문자열을 변경한다면, constant pool 에 새롭게 추가한다.

- new 로 생성할 경우, heap에 생성한다.
- 데이텨를 변경할 경우 (리터럴) 다시 constant pool 에 생성한다.

- StringBuilder 를 사용할 경우, 우리가 알고 있는 인스턴스 형태로 사용 가능하다.
  - 내부적으로 시작주소를 고정하고 byte 크기를 조작한다. (strcpy)

~~~java
    public static void main(String[] args) {

        // string constant pool
        String str1 = "apple";
        String str2 = "apple";
        System.out.println("str1: " + System.identityHashCode(str1));
        System.out.println("str2: " + System.identityHashCode(str2));

        str1 = "banana";
        System.out.println("str1: " + System.identityHashCode(str1));
        System.out.println("str2: " + System.identityHashCode(str2));

        // heap
        // 데이터 변경 시 hashcode 변경되는데, constant pool 에서 관리되는 코드로 변경되는 듯 하다.
        String str3 = new String("apple");
        String str4 = new String("apple");
        System.out.println("str3: " + System.identityHashCode(str3) + " " + str3);
        System.out.println("str4: " + System.identityHashCode(str4) + " " + str4);
        str3 = "banana";
        System.out.println("str3: " + System.identityHashCode(str3) + " " + str3);
        System.out.println("str4: " + System.identityHashCode(str4) + " " + str4);
        String str5 = "banana";
        System.out.println("str5: " + System.identityHashCode(str5) + " " + str5);


        String str6 = new String("cat");
        String str7 = new String("1cat1").intern(); // literal 과 같은 동작을 함.
        String str8 = "1cat1";
        System.out.println("str6: " + System.identityHashCode(str6) + " " + str6);
        System.out.println("str7: " + System.identityHashCode(str7) + " " + str7);
        System.out.println("str8: " + System.identityHashCode(str8) + " " + str8);


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("apple");
        System.out.println("builder : " + System.identityHashCode(stringBuilder));
        stringBuilder.append("banana");
        System.out.println("builder : " + System.identityHashCode(stringBuilder));
    }

~~~







​	

** itendifyhashcode vs hashcode 정리필요

- https://codingdog.tistory.com/entry/java-hashCode-vs-identityHashcode-%EC%9D%B4-%EB%91%98%EC%9D%80-%EB%AC%B4%EC%97%87%EC%9D%B4-%EB%8B%A4%EB%A5%BC%EA%B9%8C%EC%9A%94

** constant pool 정리 필요

- https://deveric.tistory.com/123