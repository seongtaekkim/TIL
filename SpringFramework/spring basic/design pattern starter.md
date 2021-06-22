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







# Decorator pattern

데코레이터 패턴은 기존 뼈대 (클래스)는 유지하되, 이후 필요한 형태로 꾸밀 때 사용한다. 확장이 필요한 경우 상속의 대안으로도 활용한다. SOLID중에서 개방폐쇄원칙(OCP)과 의존역전원칙(DIP)를 따른다





### 기본 Audi class를 구성한다.

```java
public interface ICar {
    int getPrice();
    void showPrice();
}

public class Audi implements ICar{
    private  int price;
    public Audi(int price) {
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void showPrice() {
        System.out.println("audi 의 가격은 "+ getPrice());
    }
}
```

- price, showPrice 생성





### decorator를 생성한다.

```java
public class AudiDecorator implements ICar {
    protected ICar audi;
    protected String modelName;
    protected int modelPrice;

    public AudiDecorator(ICar audi, String modelName, int modelPrice) {
        this.audi = audi;
        this.modelName = modelName;
        this.modelPrice = modelPrice;
    }

    @Override
    public int getPrice() {
        return audi.getPrice() + modelPrice;
    }

    @Override
    public void showPrice() {
        System.out.println(modelName + " 의 가격은 " + getPrice()+"원 입니다");
    }
}
```

-  AudiDecorator의 price는 기본 Audi의 price에  modelPrice를 더한 값이다.





### audi 모델이 decorator를 상속받는다.

```java
public class A3 extends AudiDecorator {
    public A3(ICar audi, String modelName) {
        super(audi, modelName,1000);
    }
}
ublic class A4 extends AudiDecorator {
    public A4(ICar audi, String modelName) {
        super(audi, modelName,2000);
    }
}
public class A5 extends AudiDecorator {
    public A5(ICar audi, String modelName) {
        super(audi, modelName,3000);
    }
}

```

- A3는 기본 audi 가격 + A3의 모델가격이다. 





```java
public class Main {

    public static void main(String[] args) {

        ICar audi = new Audi(1000);
        audi.showPrice();

        //a3
        ICar audi3 = new A3(audi, "A3");
        audi3.showPrice();
        //a4
        ICar audi4 = new A4(audi, "A4");
        audi4.showPrice();
        //a5
        ICar audi5 = new A5(audi,"A5");
        audi5.showPrice();
    }
```

- 기본 audi를 생성 후, 모델 별 가격을 생성한다.







# Observer pattern

관찰자 패턴은 변화가 일어났을 때, 미리 등록된 다른 클래스에 통보해주는 패턴을 구현한 것이다.

많이 보이는 곳은 event listner에서 해당 패턴을 사용하고 있다.

```java
public interface IButtonListener {
    void clickEvent(String event);
}

public class Button {
    private String name;
    private IButtonListener buttonListener;

    public Button(String name) {
        this.name = name;
    }
    public void click(String message) {
        buttonListener.clickEvent(message);
    }

    public void addListener(IButtonListener ButtonListener) {
        this.buttonListener = ButtonListener;
    }
}
```

- button 클릭 시 클릭이벤트를 호출하기위한 listener를 정의한다.



```java
public class Main {

    public static void main(String[] args) {

        Button button = new Button("버튼");

        button.addListener(new IButtonListener() {
            @Override
            public void clickEvent(String event) {
                System.out.println(event);
            }
        });

        button.click("메세지 전달 : click1");
        button.click("메세지 전달 : click2");
        button.click("메세지 전달 : click3");
        button.click("메세지 전달 : click4");
        button.click("메세지 전달 : click5");
    }
```

- 버튼 인스턴스 생성
- 버튼클릭 리스너생성(콜백 인터페이스)
- 클릭시마다 콜백함수를 호출함.(옵저버 패턴)







# Facade pattern

Facade는 건물의 앞쪽 정면 이라는 뜻을 가진다. 여러개의 객체와 실제 사용하는 서브객체 사이에 복잡한 의존관계가 있을 때 , 중간에 facade라는 객체를 두고 여기서 제공하는 interface만을 활용하여 기능을 사용하는 방식이다. Facade는 자신이 가지고 있는 각 클래스의 기능을 명확히 알아야 한다.







### Ftp접속후, 파일읽기쓰기를 수행하는 class를 구현

```java
public class Ftp {
    private String host;
    private int port;
    private String path;

    public Ftp(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public void connect() {
        System.out.println("FTP host : " + host +" Port : " + port + "로 연결합니다.");
    }

    public void moveDirectory() {
        System.out.println("path : " + path + "로 이동합니다");
    }

    public void disConnect() {
        System.out.println("FTP 연결을 종료합니다.");
    }
}

public class Reader {
    private String fileName;

    public Reader(String fileName ) {
        this.fileName = fileName;
    }

    public void fileConnect() {
        String msg = String.format("Reader %s 로 연결합니다.",fileName);
        System.out.println(msg);

    }

    public void fileRead() {
        String msg = String.format("Reader %s 내용을 읽어옵니다.",fileName);
        System.out.println(msg);
    }

    public void fileDisconnect() {
        String msg = String.format("Reader %s 로 연결종료합니다.",fileName);
        System.out.println(msg);
    }
}

public class Writer {
    private String fileName;

    public Writer(String fileName ) {
        this.fileName = fileName;
    }

    public void fileConnect() {
        String msg = String.format("Writer %s 로 연결합니다.",fileName);
        System.out.println(msg);

    }

    public void write() {
        String msg = String.format("Writer %s 파일쓰기를 합니다.",fileName);
        System.out.println(msg);
    }

    public void fileDisconnect() {
        String msg = String.format("Writer %s 로 연결종료합니다.",fileName);
        System.out.println(msg);
    }
}
```

- Ftp는 host,port,path로 접속, 디렉토리이동, 접속종료가 가능하다.
- reader는 fileName으로 접속, 읽기, 종료가 가능하다.
- writer는 faileName으로 접속,쓰기,종료가 가능하다.





```java
public class Main {

    public static void main(String[] args) {

        Ftp ftpClient = new Ftp("www.foo.co.kr",22,"/home/etc");
        ftpClient.connect();
        ftpClient.moveDirectory();

        Writer writer = new Writer("text.tmp");
        writer.fileConnect();
        writer.write();

        Reader reader = new Reader("text.tmp");
        reader.fileConnect();
        reader.fileRead();

        reader.fileDisconnect();
        writer.fileDisconnect();
        ftpClient.disConnect();

    }
```

- Ftp, writer, reader는 순서대로 함수를 실행한다.
- main에서 일련의 과정을 간략하게 수행하기 위해 facade를 구현한다.





### facade pattern 을 위해 sftpClient를 구현한다

```java
public class SftpClient {
    private Ftp ftp;
    private Reader reader;
    private Writer writer;

    public SftpClient(Ftp ftp, Reader reader, Writer writer) {
        this.ftp = ftp;
        this.reader = reader;
        this.writer = writer;

    }
    public SftpClient(String host, int port,String path, String fileName) {
    this.ftp = new Ftp(host, port, path);
    this.reader = new Reader(fileName);
    this.writer = new Writer(fileName);
    }

    public void connct() {
        ftp.connect();
        ftp.moveDirectory();
        writer.fileConnect();
        reader.fileConnect();
    }

    public void disConnect() {
        writer.fileDisconnect();
        reader.fileDisconnect();
        ftp.disConnect();
    }
    public void reader() {
        reader.fileRead();
    }
    public void writer() {
        writer.write();
    }
}

```

- connect는 ftp, reader, writer 모두 하므로, 함수하나에 모아준다.
- disconnect 도 마찬가지.



```java
public class Main {

    public static void main(String[] args) {

        SftpClient sftpClient = 
        			new SftpClient("www.foo.co.kr",22, "/home/etc","text.tmp");
        sftpClient.connct();
        sftpClient.writer();
        sftpClient.reader();
        sftpClient.disConnect();

    }
```

- 일련의 과정을 간략하게 수행할 수 있다.





# Strategy pattern

전략패턴이라고 불리며, 객체지향의 꽃이다.

유사한 행위들을 캡슐화하여, 객체의 행위를 바꾸고 싶은 경우 직접 변경하는 것이 아닌 전략만을 변경하여, 유연하게 확장하는 패턴 SOLID중에서 개방폐쇄원칙(OCP)과 의존역전원칙(DIP)를 딸느다.



전략 메서드를 가진 전략객체 (Nornal Strategy, Base64 Strategy)

전략객체를 사용하는 컨텍스트(Encoder)

전략 객체를 생성해 컨텍스트에 주입하는 클라이언트





### 전략 인터페이스 생성

```java
public interface EncodingStrategy {
    String encode(String text);
}
```





### 전략 class 생성

```java
public class Base64Strategy implements EncodingStrategy{
    @Override
    public String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }
}
public class NornalStrategy implements  EncodingStrategy{
    @Override
    public String encode(String text) {
        return text;
    }
}
```



### 전략객체를 사용하는 Context 생성

```java
public class Encoder {

    private EncodingStrategy encodingStrategy;

    public String getMessage(String message) {
        return this.encodingStrategy.encode(message);
    }
    public void setEncodingStrategy(EncodingStrategy encodingStrategy) {
        this.encodingStrategy = encodingStrategy;
    }
}

```

- 외부에서 전략객체를 주입(런타임)한 후 전략객체의 메서드를 수행한다.



```java
public class Main {

    public static void main(String[] args) {

        Encoder encoder = new Encoder();

        //base64
        EncodingStrategy base64 = new Base64Strategy();

        //normal
        EncodingStrategy normal = new NornalStrategy();
        String message = "hello java";

        encoder.setEncodingStrategy(base64);
        String base64Result = encoder.getMessage(message);
        System.out.println(base64Result);

        encoder.setEncodingStrategy(normal);
        String normalResult = encoder.getMessage(message);
        System.out.println(normalResult);

    }
```

- 같은 컨텍스트객체에 전략을 바꿔가며 사용할 수 있다.















































