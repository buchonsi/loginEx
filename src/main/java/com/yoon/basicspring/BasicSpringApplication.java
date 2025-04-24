package com.yoon.basicspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BasicSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicSpringApplication.class, args);
    }

}
