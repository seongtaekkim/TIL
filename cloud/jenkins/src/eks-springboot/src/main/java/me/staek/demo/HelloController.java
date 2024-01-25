package me.staek.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String hello() {
        return "Hello World! version3";
    }


    @GetMapping("/test")
    public String test() {
        return "Hello World! test ";
    }

    @GetMapping("/test/test2")
    public String test2() {
        return "Hello World! test test2";
    }
}
