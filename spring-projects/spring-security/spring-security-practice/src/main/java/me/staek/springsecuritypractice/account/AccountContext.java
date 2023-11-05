package me.staek.springsecuritypractice.account;

/**
 * Account 타입 ThreadLocal
 */
public class AccountContext {
    private static final ThreadLocal<Account> LOCAL = new ThreadLocal<>();

    public static void setAccount(Account account) {
        LOCAL.set(account);
    }
    public static Account getAccount() {
        return LOCAL.get();
    }
}
