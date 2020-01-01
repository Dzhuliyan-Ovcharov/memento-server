package com.memento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MementoStarter {

    public static void main(String[] args) {
        SpringApplication.run(MementoStarter.class, args);
    }
}
