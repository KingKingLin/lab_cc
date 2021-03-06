package com.cc.lab_teach.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域请求问题
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
                // 映射的请求地址，/** 表示针对所有的请求地址
        registry.addMapping(("/**"))
                .allowedOriginPatterns("*")
                .allowedHeaders(CorsConfiguration.ALL)
                // GET、POST、DELETE...
                .allowedMethods(CorsConfiguration.ALL)
                // 允许前端带上它的凭证，如 cookie，作用 session 校验
                .allowCredentials(true)
                .maxAge(3600);  // 1小时内不需要再预见（发OPTIONS请求）
    }
}
