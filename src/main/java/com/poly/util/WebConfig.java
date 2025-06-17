package com.poly.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired(required = false)
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (adminInterceptor != null) {
            registry.addInterceptor(adminInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns(
                            "/test-firebase.html",
                            "/static/test-firebase.html",
                            "/js/firebase.js",
                            "/static/js/firebase.js",
                            "/css/**",
                            "/img/**",
                            "/static/**");
        }
    }
}