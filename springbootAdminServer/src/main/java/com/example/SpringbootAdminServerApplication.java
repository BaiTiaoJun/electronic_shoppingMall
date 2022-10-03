package com.example;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import javafx.application.Application;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringbootAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringbootAdminServerApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
