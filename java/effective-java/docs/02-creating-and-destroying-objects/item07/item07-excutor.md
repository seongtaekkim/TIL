# Excutor



# 쓰레드 한번에 묶어서 학습내용을 정리하자 (11장 동시성)



## newSingleThreadScheduledExecutor();

- 시간, 간격, 주기성을 가지고 작업을 수행할 수 있다.

~~~java
      ...
      ScheduledExecutorService executorService2 = Executors.newSingleThreadScheduledExecutor();
        executorService2.schedule(getRunnable("bybye"), 3, TimeUnit.SECONDS);
        executorService2.shutdown();

        /**
         * shutdown 종료가 되기 때문에, 작성하지 않고 테스트를 하였음.
         */
        ScheduledExecutorService executorService3 = Executors.newSingleThreadScheduledExecutor();
        executorService3.scheduleAtFixedRate(getRunnable("fixedbyby"),1,2,TimeUnit.SECONDS);
        //executorService3.shutdown();

    }
~~~

~~~java
private static Runnable getRunnable(String hello) {
        return () -> System.out.println(hello + Thread.currentThread().getName());
    }
}
~~~





## CPU vs IO

- 스레드를 많이 많들어야 한다. 리소스를 많이 써야 한다.
- 100개를 안써도 같은작업을 모두 할 수 있다면? -> threadpool로 한다

- 10개의 쓰레드로 100개의 작업을 진행함 -> 10개씩 진행하므로 약간 버벅거림

- io 관련작업할때는 좀 더많은 스레드를 할당한다. 

..........

......





### Runnable

스레드 개수가 cpu 개수 넘어가면 어차피 늘려도 안된다.
-> 블럭킹큐 : 작업할 대 concurnet 하게 작업이 가능하다.

![스크린샷 2023-08-19 오전 1.55.07](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 1.55.07.png)

- cached : 필요한만큼 사용한다. : 작업공간이 하나이다. 따라서 스레드가 계속 늘어날 수 있다.

![스크린샷 2023-08-19 오전 1.55.31](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 1.55.31.png)

- single :  스레드 하나만 생성해서 모든 작업을 처리

![스크린샷 2023-08-19 오전 1.58.14](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 1.58.14.png)

- 몇초뒤 , 주기적 으로 스케쥴에 맞추어(순서상관없이) 실행한다.

![스크린샷 2023-08-19 오전 1.58.40](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 1.58.40.png)

![스크린샷 2023-08-19 오전 1.52.33](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 1.52.33.png)

![스크린샷 2023-08-19 오전 1.53.57](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 1.53.57.png)



### callable: 작업결과를 받고 싶을 때.

![스크린샷 2023-08-19 오전 2.02.39](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-19 오전 2.02.39.png)

- get : 블럭킹 콜
- Futurerr 정의 : non blocking



