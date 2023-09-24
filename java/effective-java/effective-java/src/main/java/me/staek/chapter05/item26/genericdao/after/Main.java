package me.staek.chapter05.item26.genericdao.after;

import java.util.Optional;

/**
 * Generic Dao
 * - Entitiy 를 사용하는 Repository가 여러 개 존재하고, 공통 함수가 많은 경우
 * - GenericRepository<T extends Entity> 로 공통함수를 추상화 시켜 구현의 짐을 던다.
 * - <T extends Entity> ==> Entity 타입의 함수를 사용할 수 있다는 장점이 있다.
 */
public class Main {
    public static void main(String[] args) {
        AccountRepository accountRepository = new AccountRepository();
        Account account = new Account(1L, "effective");
        accountRepository.add(account);

        Optional<Account> byId = accountRepository.findById(1L);
        System.out.println(byId.get());
    }
}
