package com.dix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan
public class BootApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootApplication.class, args);
    }
}