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





















