package com.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDubbo
@EnableTransactionManagement
@MapperScan(basePackages = {"com.example.mapper"})
@SpringBootApplication
public class ShoppingMallProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallProviderApplication.class, args);
    }

}
