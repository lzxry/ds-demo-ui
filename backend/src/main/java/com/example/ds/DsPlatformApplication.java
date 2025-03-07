package com.example.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DsPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(DsPlatformApplication.class, args);
    }
} 