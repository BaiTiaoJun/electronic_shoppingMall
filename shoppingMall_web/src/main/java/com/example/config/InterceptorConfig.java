package com.example.config;

import com.example.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @类名 MyInterceptor
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/6/16 13:29
 * @版本 1.0
 */
@Configuration(proxyBeanMethods = true)
public class InterceptorConfig implements WebMvcConfigurer {

    private final String[] includes = {"/admin/**", "/system/**"};

    private final String[] excludes = {
            "/product/**"
            , "/components/**"
            , "/"
            ,"/admin/toLogin.do"
            , "/admin/login.do"
            , "/admin/loadImageCode.do"
            ,"/admin/toRegister.do"
            , "/admin/isExistUsername.do"
            , "/admin/requestShortLetterCode.do"
            , "/admin/register.do"
            , "/admin/isExistPhoneNumber.do"
            , "/admin/tradeNotify.do"
    };

    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).
                addPathPatterns(includes).
                excludePathPatterns(excludes);
    }
}
