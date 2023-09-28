package me.staek.memo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController {

    @GetMapping("/api/hello")
    public String test() {
        System.out.println("111111111");
        return "Hello, world!";
    }
}
