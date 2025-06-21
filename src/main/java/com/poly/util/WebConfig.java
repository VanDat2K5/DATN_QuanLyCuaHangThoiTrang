package com.poly.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired(required = false)
    private AdminInterceptor adminInterceptor;

    @Autowired(required = false)
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (adminInterceptor != null) {
            registry.addInterceptor(adminInterceptor)
                    .addPathPatterns("/admin/**")
                    .excludePathPatterns(
                            "/css/**",
                            "/img/**",
                            "/static/**");
        }
        if (userInterceptor != null) {
            registry.addInterceptor(userInterceptor)
                    .addPathPatterns("/user/**", "/cart/**", "/order/**")
                    .excludePathPatterns(
                            "/login",
                            "/register",
                            "/access-denied",
                            "/css/**",
                            "/img/**",
                            "/static/**");
        }
    }
}