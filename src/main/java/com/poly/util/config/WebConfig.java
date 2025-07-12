package com.poly.util.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poly.util.AdminInterceptor;
import com.poly.util.UserInterceptor;

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
                            "/login",
                            "/register",
                            "/access-denied",
                            "/css/**",
                            "/images/**",
                            "/img/**",
                            "/js/**",
                            "/vendors/**",
                            "/country-flag-16x16/**",
                            "/fashion-store/**",
                            "/fonts/**",
                            "/demos/**",
                            "/static/**",
                            "/"
                    );
        }
        if (userInterceptor != null) {
            registry.addInterceptor(userInterceptor)
                    .addPathPatterns("/user/**", "/cart/**", "/order/**")
                    .excludePathPatterns(
                            "/login",
                            "/register",
                            "/access-denied",
                            "/css/**",
                            "/images/**",
                            "/img/**",
                            "/js/**",
                            "/vendors/**",
                            "/country-flag-16x16/**",
                            "/fashion-store/**",
                            "/fonts/**",
                            "/demos/**",
                            "/static/**",
                            "/"
                    );
        }
    }
}