package com.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
@EnableDubboConfig
public class ShoppingMallTimerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallTimerApplication.class, args);
    }
}
