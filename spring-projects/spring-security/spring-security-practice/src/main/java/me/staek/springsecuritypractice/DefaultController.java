package me.staek.springsecuritypractice;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal == null)
            model.addAttribute("message", "hello no login user");
        else
            model.addAttribute("message", "login : " + principal.getName());
        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "hello world");
        return "info";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("message", "admin : " + principal.getName());
        return "admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("message", "dashboard : " + principal.getName());
        return "dashboard";
    }
}
