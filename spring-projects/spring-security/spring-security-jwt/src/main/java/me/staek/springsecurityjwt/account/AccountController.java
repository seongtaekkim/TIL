package me.staek.springsecurityjwt.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{role}/{username}/{password}")
    public Account createAccount(@ModelAttribute Account account) {
        return accountService.createAccount(account);
    }



    @Autowired AccountRepository accountRepository;
    @GetMapping("/threadlocal")
    public String threadlocal(Model model, Principal principal) {
        model.addAttribute("message", "principal : " + principal.getName());

        /**
         * ThreadLocal 초기화
         */
        AccountContext.setAccount(accountRepository.findByUsername(principal.getName()));
        accountService.printUsername();
        return "dashboard";
    }
}
