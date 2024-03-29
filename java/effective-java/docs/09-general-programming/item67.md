# item67 최적화는 신중히 하라.



### 망한 최적화 명언

- (맹목적인 어리석음을 포함해) 그 어떤 핑계보다 효율성이라는 이름 아래 행해진 컴퓨팅 죄악이 더 많다.(심지어 더 효율적이지도 않다.) - 윌리엄 울프(Wulf72)
- (전체의 97% 정도인) 자그마한 효율성은 모두 잊자. 섣부른 최적화가 만악의 근원. - 도널드 크루스(Knuth74)
- 최적화를 할 때 두 가지 규칙을 따르라.  첫 번째, 하지 마라.  두 번째, (전문가 한정) 아직 하지 마라. 완전히 명백하고 최적화되지 않은 해법을 찾을 때까진 하지 마라.  - M. A. 잭슨(Jacson75)



### 빠른 프로그램이 아닌 좋은 프로그램을 작성하라.

위 격언들은 자바가 탄생하기 20년도 전에 나온 것이다.  즉, 이전부터 최적화라는 단어는 유혹적이지만, 대체로 배드엔딩뿐인 단어다. 얼마 하지 않는 성능을 올리기 위해 구조를 희생해버리면 안된다.

좋은 프로그램은 정보 은닉 원칙(캡슐화)을 따르기에 각각의 모듈이 독립적으로 설계되며, 시스템의 나머지 부분에 영향을 주지 않고도 각 요소를 다시 설계할 수 있다. 

무엇보다, 구현의 문제는 고칠 수 있지만, 설계에 결함이 생기면 시스템 전체를 수정해야 하기에 비용이 크다. 그렇기에 설계 단계에서 성능을 고려해야 한다.  그렇기에 다음 내용을 고려해야 한다. 



### 1. 성능을 제한하는 설계를 피하라.

완성한 뒤 가장 변경하기 어려운 부분이 컴포넌트끼리, 혹은 외부 시스템과의 소통 방식이다. 

예로 API, 네트워크 프로토콜, 영구 저장용 데이터 포맷 등이 대표적이다. 

이런 요소들은 일단 완성이 된 후에는 시간이 흐를수록 변경하기 어려워지고, 시스템의 성능을 제한하게 된다. 



### 2. API를 설계할 때 성능에 주는 영향을 고려하라. 

이전 아이템들에서도 소개한 여러 방식들을 고려해서 성능을 고민해볼 필요가 있다. 

- public 타입을 가변으로 만들면, 불필요한 방어적 복사를 유발한다. (item50)
- 컴포지션이 아닌 상속으로 (그럴 필요가 없음에도!) 설계한 public 클래스는 상위 클래스에 종속되고 성능의 제약까지 물려받게 된다.  (item18 )
- 인터페이스도 있는데 구현타입을 사용하는것도 유연성이 떨어지게 되어 나중에 더 뛰어난 구현체가 나오더라도 이용하기 힘들게 된다. (item64)



### 3. 최적화 시도 전후로 성능을 측정하라.

그럼에도 불구하고 최적화를 진행해봤다면, 시도 전 후로 성능을 측정할 필요가 있다. 

대부분 시도한 최적화 기법이 성능을 눈에 띄게 높이지 못하는 경우가 많다. 그 이유는 개발자가 프로그램에서 어디에 시간이 최대 소요되는지 추측하기 어렵기 때문이다. 

이런 경우 프로파일링 도구(profiling tool)  를 이용할 수 있다. 이런 도구는 개별 메서드의 소비 시간과 호출 횟수 같은 런타임 정보를 제공한다. 일종의 금속탐지기 역할을 한다고 할 수 있다. 

- **jmh** (마이크로 벤치마킹 프레임워크)



### 정리

- 좋은 프로그램을 작성하면 성능은 따라오기 마련이다. 
- 설계 시점에서 API, 네트워크 프로토콜, 영구 저장용 데이터 포맷을 설계할 때 성능을 고민하자. 
- 구현이 완료된 다음에는 ‘느린 경우에만’ 프로파일러를 사용해 문제를 찾아 최적화를 수행하자. 
- 그리고 최적화 뒤에는 꼭 성능을 측정해서 올바른 최적화인지 판단할 필요가 있다. 
