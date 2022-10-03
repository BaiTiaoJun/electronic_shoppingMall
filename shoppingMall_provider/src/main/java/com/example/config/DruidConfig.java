//package com.example.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.Filter;
//import javax.sql.DataSource;
//import java.util.*;
//
///**
// * @类名 DruidConfig
// * @描述 druid 配置服务监控
// * @作者 白条君
// * @创建日期 2022/7/18 22:46
// * @版本 1.0
// */
//@Configuration
//public class DruidConfig {
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.druid")
//    public DataSource dataSource() {
//        return new DruidDataSource();
//    }
//
//    //后台监控
//    @Bean
//    public ServletRegistrationBean<StatViewServlet> servletServletRegistrationBean() {
//        //添加servlet注册器
//        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>();
//        //注册servlet
//        bean.setServlet(new StatViewServlet());
//        //添加servlet映射地址
//        Collection<String> urlMappings = new ArrayList<>();
//        urlMappings.add("/druid/*");
//        bean.setUrlMappings(urlMappings);
//        //封装servlet初始化参数
//        Map<String, String> map = new HashMap<>();
//        //用户验证：给StatViewServlet中的loginUsername和loginPassword赋值
//        map.put("loginUsername", "root");
//        map.put("loginPassword", "root");
//        //添加访问角色，默认都可以访问
//        map.put("allow", "127.0.0.1");
//        //不允许访问的角色
////        map.put("deny", "192.15.40.14");
//        //添加druid的servlet初始化参数
//        bean.setInitParameters(map);
//        //配置加载优先级
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
//        //创建过滤器注册器
//        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>(new WebStatFilter());
//        //添加过滤的路径
//        bean.addUrlPatterns("/*");
//        //封装filter的初始化参数，不进行过滤的格式
//        Map<String, String> map = new HashMap<>();
//        map.put("exclusions", "*.js, *.css");
//        //添加druid的filter初始化参数
//        bean.setInitParameters(map);
//        return bean;
//    }
//}