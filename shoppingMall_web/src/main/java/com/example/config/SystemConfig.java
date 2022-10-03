package com.example.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @类名 SystemConfiguration
 * @描述 TODO
 * @作者 白条君
 * @创建日期 2022/7/18 18:21
 * @版本 1.0
 */
@Configuration(proxyBeanMethods = true)
public class SystemConfig {

    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
        //创建字符过滤器
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        final String ENCODING = "utf-8";
        encodingFilter.setEncoding(ENCODING);
        encodingFilter.setForceEncoding(true);

        //注册字符过滤器
        FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(encodingFilter);
        return registrationBean;
    }
}
