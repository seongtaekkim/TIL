# Finalizer Attack



### 타겟 코드

~~~java
public class Account {

    private String accountId;

    public Account(String accountId) {
        this.accountId = accountId;

        if (accountId.equals("hacker")) {
            throw new IllegalArgumentException("hacker: exception");
        }
    }

    public void transfer(BigDecimal amount, String to) {
        System.out.printf("transfer %f from %s to %s\n", amount, accountId, to);
    }
}
~~~

- 특정 유저 인자에 대한 생성자 생성을 막는 코드가 있다.



### 공격 코드

~~~java
public class BrokenAccount extends Account {

    public BrokenAccount(String accountId) {
        super(accountId);
    }

    @Override
    protected void finalize() throws Throwable {
        this.transfer(BigDecimal.valueOf(100), "hacker");
    }
}
~~~

- fianlize() 를 재정의 한 후, 공격 대상 메서드를 호출하는 코드를 작성한다.



### 테스트

~~~java
class AccountTest {

    @Test
    void nomal_test() {
        Account account = new Account("seongtki");
        account.transfer(BigDecimal.valueOf(10.4),"hello");
    }

    @Test
    void hacker_test() throws InterruptedException {
        Account account = null;
        try {
            account = new BrokenAccount("hacker");
        } catch (Exception exception) {
            System.out.println("Account 생성중에 exception");
        }

        System.gc();
        Thread.sleep(3000);
    }

}
~~~

- hacker 유저는 Account를 생성할 수 없으므로 except 된다. (하지만 객체는 생성되는 것 같다.)
- 이후 gc()를 실행하면 재정의한 fialize()가 실행되게 되고, transfer 메서드가 호출된다.



### 공격 예방

~~~java
public class Account {
    ...
    @Override
    protected final void finalize() throws Throwable {

    }
}
~~~

- finalize()를 final 로 override 해서 상속을 막는다.































