package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @类名 WebConfig
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/7/19 14:23
 * @版本 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin/toLogin.do").setViewName("admin/login");
        registry.addViewController("/admin/toRegister.do").setViewName("admin/register");
        registry.addViewController("/system/toSuccess.do").setViewName("system/success");
    }
}
