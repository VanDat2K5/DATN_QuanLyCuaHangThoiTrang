package com.poly.util.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
<<<<<<< HEAD:src/main/java/com/poly/util/WebConfig.java
                            "/images/**",
                            "/img/**",
                            "/js/**",
                            "/fonts/**",
                            "/demos/**",
                            "/static/**");
=======
                            "/js/**", "/vendors/**",
                            "/images/**", "/country-flag-16x16/**",
                            "/fashion-store/**",
                            "/fonts/**",
                            "/static/**",
                            "/");
>>>>>>> 48eb8ff19126e552a0c00db37cb7e20e51d416cf:src/main/java/com/poly/util/config/WebConfig.java
        }
        if (userInterceptor != null) {
            registry.addInterceptor(userInterceptor)
                    .addPathPatterns("/user/**", "/cart/**", "/order/**")
                    .excludePathPatterns(
                            "/login",
                            "/register",
                            "/access-denied",
                            "/css/**",
<<<<<<< HEAD:src/main/java/com/poly/util/WebConfig.java
                            "/images/**",
                            "/img/**",
                            "/js/**",
                            "/fonts/**",
                            "/demos/**",
                            "/static/**");
=======
                            "/js/**", "/vendors/**",
                            "/images/**", "/country-flag-16x16/**",
                            "/fashion-store/**",
                            "/fonts/**",
                            "/static/**",
                            "/");
>>>>>>> 48eb8ff19126e552a0c00db37cb7e20e51d416cf:src/main/java/com/poly/util/config/WebConfig.java
        }
    }
}