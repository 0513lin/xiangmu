package com.bjpowernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicrTestApplication {

    public static void main(String[] args) {
        org.springframework.context.ConfigurableApplicationContext run = SpringApplication.run(MicrTestApplication.class, args);
    }

}
