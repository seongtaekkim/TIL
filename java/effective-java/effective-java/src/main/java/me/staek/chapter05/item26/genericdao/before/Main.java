package me.staek.chapter05.item26.genericdao.before;

import me.staek.chapter05.item26.genericdao.after.Account;
import me.staek.chapter05.item26.genericdao.after.AccountRepository;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        AccountRepository accountRepository = new AccountRepository();
        Account account = new Account(1L, "effective");
        accountRepository.add(account);

        Optional<Account> byId = accountRepository.findById(1L);
        System.out.println(byId.get());
    }
}
