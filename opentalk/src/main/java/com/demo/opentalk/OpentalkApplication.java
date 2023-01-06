package com.demo.opentalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OpentalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpentalkApplication.class, args);
    }

}
