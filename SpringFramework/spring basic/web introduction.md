# Web 이란

World Wid Web은 인터넷 연결된 컴퓨터를 통해 사람들이 정보를 공유할 수 있는 전세계적인 정보공간을 말한다.

Web의 용도는 다양하게 나눌 수 있습니다.



Web Site

google, naver, daum, facebook 등  HTML로 구성된 사이트



API(Application Programming Interface) Web Service

KaKao Open API, 등



User Interface

Chrome, Safari, Explorer, Ip Tv 등



## Web의 기본 3가지 요소

### URI

Uniform Resource Identifier (리소스 식별자)

- 특정한 사이트
- 특정 쇼핑목록
- 동영상목록

- 모든 정보에 접근 할 수 있는 정보



### HTTP

Hypertext Transfer Protocol (어플리케이션 컨트롤)

- GET, POST, PUT, DELETE, OPTIONS, HEAD, TRACE, CONNECT



### HTML

Hyper Text Markup Language (하이퍼미디어 포맷)

XML을 바탕으로 한 범용문서 포맷

이를 이용하여 Chrome, safari, Explorer에서 사용자가 알아보기 쉬운 형태로 표현.





# REST

REST (Representational State Transfer : 자원의 상태 전달) - 네트워크 아키텍처



1. Client, Server : 클라이언트와 서버가 서로 독립적으로 분리되어 있어야 한다.
2. Stateless : 요청에 대해서 클라이언트의 상태를 서버에 저장하지 않는다.
3. Cache : 클라이언트는 서버의 응답을 Cache(임시저장)할 수 있어야 한다.

​       클라이언트가 Cache를 통해서 응답을 재사용할 수 있어야 하며, 이를 통해서 서버의 부하를 낮춘다.

4. 계층화 (Layered System) : 서버와 클라이언트 사이에 방화벽, 게이트웨이 ,Proxy 등 다양한 계층 형태로 구성이 가능해야 하며, 이를 확장할 수 있어야 한다.
5. 인터페이스 일관성 : 인터페이스의 일관성을 지키고, 아키텍처를 단순화 시켜 작은 단위로 분리하여 클라이언트, 서버가 독립적으로 개선될 수 있어야 한다.
6. Code on Demand (Optional) : 자바 애플릿, 자바스크립트, 플래시 등 특정한 기능을 서버로부터 클라이언트가 전달받아 코드를 실행할 수 있어야 한다.





다음의 인터페이스  일관성이 잘 지켜졌는지에 따라, REST를 잘 사용했는지 판단을 할 수 있다.



1. 자원의 식별
2. 메시지를 통한 리소스 조작
3. 자기 서술적 메시지





1. 자원의 식별

웹 기반의 REST 에서는 리소스 접근을 하 때 URI를 사용한다.

https://foo.co.kr/user/100

Resource : user

식별자 : 100



2. 메세지를 통한 리소스 조작

Web에서는 다양한 방식으로 데이터를 전달 할 수 있다.

그 중에서 가장 많이 사용하는 방식은 HTML, XML, JSON, TEXT 등이 있습니다.

이 중에서 어떠한 타입의 데이터인지를 알려주기 위하여 HTTP Header부분에

content-type을 통해서 데이터의 타입을 지정해 줄 수 있습니다.

또한 리소스 조작을 위해서 데이터 전체를 전달하지 않고 이를 메세지로 전달합니다.



Ex) 서버의 user라는 정보의 전화번호를 처음에는  number로 결정했고, 이정보를

Client와 주고 받을때 그대로 사용하고 있었다면, 후에 서버의 resource변경으로 phone-number로 바뀌게 된다면

Client는 처리하지 못하고 에러가 납니다.



이러한 부분을 방지하기 위하여, 별도의 메시지의 형태로 데이터를 주고 받으며, client-server가 독립적으로 확장 가능하도록 합니다.



3. 자기서술적 메세지

요청하는 데이터가 어떻게 처리되져야 하는 지 충분한 데이터를 포함할 수 있어야 한다.

HTTP 기반의 REST에서는 HTTP Method와 Header 정보, URI의 포함하는 정보로 표현할 수 있다.

GET : https://foo.co.kr/user/100, 사용자의 정보요청

POST : https://foo.co.kr/user/, 사용자 정보생성

PUT : https://foo.co.kr/user/, 사용자 정보생성 및 수정

DELETE : https://foo.co.kr/user/100, 사용자정보 삭제

** 그 외에 담지 못한 정보는 URI의 메세지를 통해 표현한다.





4. Application  상태에 대한 엔진으로서 하이퍼미디어



REST API를 개발 할 때 단순히 client 요청에 대한 데이터만 응답 해주는것이 아닌 관련된 리소스에 대한 link정보까지 같이 포함 되어져야 한다.

이러한 조건들을 잘 갖춘 경우 REST FUL 하다고 표현하고, 이를 REST API라고 부른다.





# URI 설계



1. URI(Uniform Resource Identifier)

인터넷에서 특정 자원을 나타내는 주소 괎. 해당 값은 유일하다.(응답은 달라질 수 있다.)

요청 : https://www.fastcampus.co.kr/resource/sample/1

응답 : fastcampus,pdf, fastcampus.docx



2. URL(Uniform Resource Locator)

인터넷상에서의 자원, 특정파일이 어디에 위치하는 지 식별하는 주소

요청 : https://www.fastcampus.co.kr/fastcampus.pdf



URL은 URI의 하위 개념이다.





## 1. URI 설계원칙 (RFC-3986)

- 슬래시 구분자(/)는 계층관계를 나타내는데 사용한다.

https://fastcampus.co./kr/classes/java/curriculums/web-master



- URI마지막 문자로(/)는 포함하지 않는다.

https://fastcampus.co.kr/classes/java/curriculums/web-master/



- 하이픈(-)은 URI가독성을 높이는데 사용한다.

https://fastcampus.co.kr/classes/java/curriculums/web-master/



- 밑줄(_)은 사용하지 않는다.

https://fastcampus.co.kr/classes/java/curriculums/web**_**master



- URI경로에는 소문자가 적합하다.

https://fastcampus.co.kr/classes/**JAVA**/curriculums/web-master  (X)

https://fastcampus.co.kr/classes/**java**/curriculums/web-master  (O)



- 파일확장자는 URI에 포함하지 않는다.

https://fastcampus.co.kr/classes/java/curriculums/web-master.**jsp**





- 프로그래밍 언어에 의존적인 확장자를 사용하지 않는다.

https://fastcampus.co.kr/classes/java/curriculums/web-master**.do**



- 구현에 의존적인 경로를 사용하지 않는다.

https://fastcampus.co.kr/**servlet**/classes/java/curriculums/web+master



- 세션 id를 포함하지 않는다.

https://fastcampus.co.kr/classes/java/curriculums/web+master?**session-id=abcde**



- 프로그래밍 언어의 method명을 사용하지 않는다.

https://fastcampus.co.kr/classes/java/curriculums/web+master?**action=intro**



- 명사에 단수형보다는 복수형을 사용해야 한다. 컬렉션에 대한 표현은 복수로 사용

https://fastcampus.co.kr/class**es**/java/curriculum**s**/web+master/



- 컨트롤러 이름으로는 동사나 동사구를 사용한다.

https://fastcampus.co.kr/classes/java/curriculums/web+master/**re-order**



- 경로 부분 중 변하는 부분은 유일한 값으로 대체한다



https://fastcampus.co.kr/classes/java/curriculums/web+master/lessons/{**lesson-id}/users/{user-id}**

https://fastcampus.co.kr/classes/java/curriculums/web+master/2/users**/100**



- CRUD 기능을 나타내는 것은 URI에 사용하지 않는다.

GET : https://fastcampus.co.kr/classes/java/curriculums/web+master**/2/users/100/READ**  (x)

DELETE : https://fastcampus.co.kr/classes/java/curriculums/web+master/**2/users/100**         (o)



- URI Query Parameter 디자인

URI 쿼리부분으로 컬렉션 결과에 대해서 필터링 할 수 있다.

https://fastcampus.co.kr/classes/java/curriculums/web+master?**chapter=2**



- URI 쿼리는 컬렉션의 결과를 페이지로 구분하여 나타내는데 사용한다.

https://fastcampus.co.kr/classes/java/curriculums/web+master?**chapter=2&page=0&size=10&sort=asc**



- API에 있어서 서브 도메인은 일관성 있게 사용해야 한다.

https://fastcampus.co.kr

https://api.fastcampus.co,kr

https://api-fastcampus.co,kr



- 클라이언트 개발자 포탈 서브 도메인은 일관성 있게 만든다.

https://dev-fastcampus.co.kr

https://developer-fastcampus.co.kr











































