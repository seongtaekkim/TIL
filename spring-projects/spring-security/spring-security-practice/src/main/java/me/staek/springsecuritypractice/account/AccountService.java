package me.staek.springsecuritypractice.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null)
            throw new UsernameNotFoundException(username);

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account createAccount(Account account) {
        account.encodePassword(passwordEncoder);
        this.accountRepository.save(account);
        return account;
    }

    /**
     * ThreadLocal은 요청(thread) 별로 제네릭 타입 정보가 격리되어 있기 때문에,
     * 인자를 받지 않고도 해당 thread의 제네릭타입 정보를 얻을 수 있다.
     */
    public void printUsername() {
        Account account = AccountContext.getAccount();
        System.out.println(account.toString());
    }
}
