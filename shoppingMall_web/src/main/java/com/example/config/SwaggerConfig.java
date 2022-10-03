package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @类名 SwaggerConfig
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/7/19 12:52
 * @版本 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final Profiles profiles = Profiles.of("dev", "test");

    //配置作者信息
    public static final Contact DEFAULT_CONTACT
            = new Contact(
            "baitiaojun",
            "http://baitiaojun.com",
            "xxxx@qq.com");

    //配置系统基本信息
    public static final ApiInfo DEFAULT
            = new ApiInfo("淘淘乐",
            "电子商城购物网站",
            "1.0.0",
            "http://106.75.236.113:8081/",
            DEFAULT_CONTACT,
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            new ArrayList<>());

    @Bean
    public Docket docket1(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2).
                enable(environment.acceptsProfiles(profiles)).
                apiInfo(apiInfo()).
                groupName("admin").
                select().
                apis(RequestHandlerSelectors.basePackage("com.example.controller.admin")).
                build();

    }

    @Bean
    public Docket docket2(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2).
                apiInfo(apiInfo()).
                enable(environment.acceptsProfiles(profiles)).
                groupName("product").
                select().
                apis(RequestHandlerSelectors.basePackage("com.example.controller.product")).build();
    }

    @Bean
    public Docket docket3(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2).
                apiInfo(apiInfo()).
                enable(environment.acceptsProfiles(profiles)).
                groupName("components").
                select().
                apis(RequestHandlerSelectors.basePackage("com.example.controller.components")).build();
    }

    @Bean
    public ApiInfo apiInfo() {
        return DEFAULT;
    }
}
