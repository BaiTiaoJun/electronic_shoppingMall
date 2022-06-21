package com.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableDubbo
//要排除jdbc的自动装配 ，否则不能直接初始化
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ShoppingMallWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallWebApplication.class, args);
    }
}
