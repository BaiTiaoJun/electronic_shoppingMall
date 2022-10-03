package com.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
//要排除jdbc的自动装配 ，否则不能直接初始化
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ShoppingMallWebApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ShoppingMallWebApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    /**
     * 1 使用log4j，添加log4j.properties配置文件
     * 2 启动类添加 System.setProperty(“dubbo.application.logger”,“log4j2”) 指定项目中使用的日志框架
     * 3 设置jvm启动参数 -Ddubbo.application.logger=log4j2
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        System.setProperty("dubbo.application.logger", "log4j");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingMallWebApplication.class);
    }
}