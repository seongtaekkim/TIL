package me.staek.memo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoApplication.class, args);

//        SpringApplication app = new SpringApplication(MemoApplication.class);
//        app.setWebApplicationType(WebApplicationType.NONE);
//        app.run(args);
    }

}
