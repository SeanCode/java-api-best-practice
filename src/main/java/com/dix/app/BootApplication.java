package com.dix.app;

import com.dix.app.common.Config;
import com.dix.base.common.CoreConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan
@ComponentScan(basePackages = "com.dix.base")
@EnableScheduling
public class BootApplication {

    public static void main(String[] args) throws Exception {

        CoreConfig.getInstance().addToGuestCanAccessPathPatternList(Lists.newArrayList(Config.PATH_GUEST_CAN_ACCESS_PATTERN));

        SpringApplication.run(BootApplication.class, args);
    }
}