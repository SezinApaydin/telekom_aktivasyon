package com.etiya.telekomaktivasyon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelekomAktivasyonApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelekomAktivasyonApplication.class, args);
    }

}