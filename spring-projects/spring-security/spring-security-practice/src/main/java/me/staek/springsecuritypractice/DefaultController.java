package me.staek.springsecuritypractice;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "hello world");
        return "index";
    }
}
