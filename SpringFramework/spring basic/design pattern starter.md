# SingleTon pattern

- Singleton pattern은 어떤한 클래스가 유일하게 한개만 존재할 때 사용한다.

​      이를 주로 사용하는 곳은 서로 자원을 공유할 때 사용하는데, 실물세계에서는 프린터가 해당되며,

​      실제 프로그래밍에서는 TCP Socket통신에서 서버와 연결된 connect 객체에서 주로 사용한다.



```java
public class SocketClient {

    private static SocketClient socketClient = null;

    // 싱글톤은 기본적으로 생성자가 private이어야 한다. (다른 class에서 객체를 생성못하게.)
    private SocketClient() { }
    // 객체는 한개만 생성하여 제공하도록 한다.
    public static SocketClient getInstance() {
        if(socketClient == null) {
            socketClient = new SocketClient();
        }
        return socketClient;
    }

    public void connect() {
        System.out.println("connect");
    }
}
```

(1)  싱글톤을 적용할 클래스는 생성자를 private로 생성하여 타 클래스에서 인스턴스를 생성하지 못하게 한다.

(2) 유일한 객체를 제공할 함수 getInstance()를 만든다.



```java
public class AClazz {
    private SocketClient socketClient;

    // 싱글톤은 기본적으로 생성자가 private이므로 제공해주는 객체를 받아서 사용해야 한다.
    public AClazz(){
        this.socketClient = SocketClient.getInstance();
        //this.socketClient = new SocketClient();
    }

    public SocketClient getSocketClient() {
        return this.socketClient;
    }
}
```



```java
public class BClazz {
    private SocketClient socketClient;

    public BClazz(){
      this.socketClient = SocketClient.getInstance();
        //this.socketClient = new SocketClient();
    }

    public SocketClient getSocketClient() {
        return this.socketClient;
    }

}
```



```java
public class Main {

    public static void main(String[] args) {

        AClazz aClazz = new AClazz();
        BClazz bClazz = new BClazz();
        // 싱글톤일 경우 제공하는 객체가 한가지 이므로, 동일한 인스턴스를 같는다.
        SocketClient aClient = aClazz.getSocketClient();
        SocketClient bClient = bClazz.getSocketClient();

        System.out.println(aClient.equals(bClient));
    }
}

```

(1) SocketClient 인스턴스를 제공받아 비교할 클래스 AClazz, BClazz를 생성한다.

(2) SocketClient 객체를 비교한다. (결과 : true)







# Adapter pattern

Adapter는 실생활에서는 100v를 220v로 변경해주거나, 그 반대로 해주는 흔히 돼지코라고 불리는 변환기로 예를 들 수 있다. 호환성이 없는 기존 클래스의 인터페이스를 변환하여 재사용 할 수 있도록 한다.

SOLID중에서 개방폐쇄원칙(OCP)를 따른다.





#### 110v로동작하는 헤어드라이어와 220v로 동작하는 에어컨을 정의한다.

```java
public interface Electronic110V {
    void powerOn();
}
public interface Electronic220V {
    void conect();
}

public class HairDryer implements  Electronic110V {

    @Override
    public void powerOn() {
        System.out.println("에어드라이키 110v on");
    }
}
public class AirConditioner implements  Electronic220V {
    @Override
    public void connect() {
        System.out.println("에어컨디셔너 220v");
    }
}
```



#### 220v의 에어컨을 110v에서 동작할수 있도록  어뎁터를 생성해준다.

```java
public class SocketAdapter implements Electronic110V{
    private Electronic220V electronic220V;

    public SocketAdapter(Electronic220V electronic220V) {
        this.electronic220V = electronic220V;
    }

    @Override
    public void powerOn() {
        electronic220V.connect();
    }
}
```

- 110v를 상속받았으나, 실제로는  220v로 객체를생성하여 함수를 수행함.



```java
public class Main {

    public static void main(String[] args) {

        HairDryer hairDryer = new HairDryer();
        connect(hairDryer);

        // 220v를 110v로 변환해주어야 함.
        // 변환해주는 adater를 만들어주는걸 어뎁터패턴이라고 한다.
        AirConditioner airConditioner = new AirConditioner();
        Electronic110V airAdapter = new SocketAdapter(airConditioner);
        connect(airAdapter);
    }

    // 콘센트
    public static void connect(Electronic110V electronic110V) {
        electronic110V.powerOn();
    }
}

```

- 110v를 실행하기 위해 adapter를 이용하여 220v 기계를 실행할 수 있음.







# Proxy pattern

Proxy는 대리인이라는 뜻으로서, 뭔가를 대신 처리하는것.

Proxy Class를 통해서 대신 전달하는 형태로 설계되며, 실제 Client는 Proxy로부터 결과를 받는다.

Cache의 기능으로도 활용이 가능하다.

SOLID중에서 개방폐쇄원칙(OCP)과 의존역전원칙(DIP)를 따른다.





#### url을 요청받아서, HTML을 생성하여 로딩해주는 class를 구성한다.

```java
public interface IBrowser {
    Html show();
}
public class Html {
    private String url;

    public Html(String url) {
        this.url = url;
    }
}

public class Browser implements IBrowser{
    private String url;

    public Browser(String url) {
        this.url = url;
    }

    @Override
    public Html show() {
        System.out.println("browser loading html from : " + url);
        return new Html(url);
    }
}
```

- Html : html문서 생성
- Browser : html문서 로딩





#### 캐시 기능이 있는 BrowserProxy class를 구성한다.

```java
public class BrowserProxy implements  IBrowser {
    private String url;
    private Html html;

    public BrowserProxy(String url) {
        this.url = url;
    }

    @Override
    public Html show() {
        if(html == null) {
            this.html = new Html(url);
            System.out.println("BrowserProxy loading html from : " + url);
        }
        System.out.println("BrowserProxy use cache html : " + url);
        return html;
    }
}
```

- Html문서가 없을 경우 생성하고, 있을경우 존재하는 문서를 리턴함.



#### proxy pattern에 aop패턴을 추가한 class를 구성한다.

```java
public class AopBrowser implements IBrowser {

    private String url;
    private Html html;
    private Runnable before;
    private Runnable after;

    public AopBrowser(String url, Runnable before, Runnable after) {
        this.url = url;
        this.before = before;
        this.after = after;
    }
    @Override
    public Html show() {

        before.run();

        if(html == null) {
            this.html = new Html(url);
            System.out.println("AopBrowser html loading from : " + url);
            try {
                Thread.sleep(1500);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        after.run();
        return html;
    }
}
```

- 프록시패턴 앞뒤로  Runnable함수를 수행한다.



```JAVA
public class Main {

    public static void main(String[] args) {

        Browser browser = new Browser("www.naver.com");
        // 계속호출하면 캐시기능없이 계속 호출함
        browser.show();
        browser.show();
        browser.show();
        browser.show();
        browser.show();

        BrowserProxy browserProxy = new BrowserProxy("www.naver.com");
        // 캐시기능을 이용하면, 이미 생성된 객체를 호출한다.
        browserProxy.show();
        browserProxy.show();
        browserProxy.show();
        browserProxy.show();
        browserProxy.show();

        AtomicLong start = new AtomicLong();
        AtomicLong end = new AtomicLong();

        //AOP
        IBrowser aopBrowser = new AopBrowser("www.naver.com"
        ,()->{
           System.out.println("before");
            start.set(System.currentTimeMillis());
        },()->{
            long now = System.currentTimeMillis();
            end.set(now - start.get());
        });
        // AOP 패턴을 이용하면, 프록시패턴 이전,이후에 원하는 함수를 일괄적으로 사용할 수 있다.
        aopBrowser.show();
        System.out.println("loading time : " + end.get());
        aopBrowser.show();
        System.out.println("loading time : " + end.get());
        aopBrowser.show();
        System.out.println("loading time : " + end.get());
    }
```















































