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













