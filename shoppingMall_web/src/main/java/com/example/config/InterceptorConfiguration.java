package com.example.config;

import com.example.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @类名 MyInterceptor
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/16 13:29
 * @版本 1.0
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        MyInterceptor myInterceptor = new MyInterceptor();

        String[] includes = {"/admin/**", "/system/**"};
        String[] excludes = {"/product/**", "/components/**", "/",
                "/admin/toLogin.do", "/admin/login.do", "/admin/loadImageCode.do",
                "/admin/toRegister.do", "/admin/isExistUsername.do", "/admin/requestShortLetterCode.do",
                "/admin/register.do", "/admin/isExistPhoneNumber.do", "/admin/tradeNotify.do"};

        InterceptorRegistration registration = registry.addInterceptor(myInterceptor);
        registration.addPathPatterns(includes);
        registration.excludePathPatterns(excludes);
    }
}
