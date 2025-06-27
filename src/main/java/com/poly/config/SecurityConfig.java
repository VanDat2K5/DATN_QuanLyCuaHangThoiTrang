package com.poly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.poly.entity.TaiKhoan;
import com.poly.entity.KhachHang;
import com.poly.service.TaiKhoanService;
import com.poly.service.KhachHangService;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**", "/login", "/register", "/logout", "/user/**", "/admin/**", "/css/**",
                                "/js/**",
                                "/images/**", "/demos/**",
                                "/fonts/**", "/static/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(customOAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(this.oauth2UserService())));
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return userRequest -> {
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            // Kiểm tra nếu đã có tài khoản thì thôi, chưa có thì tạo mới
            Optional<TaiKhoan> tkOpt = taiKhoanService.findByTenTK(email);
            if (tkOpt.isEmpty()) {
                // Tạo khách hàng mới
                KhachHang kh = new KhachHang();
                kh.setMaKH(UUID.randomUUID().toString().substring(0, 20));
                kh.setTenKH(name);
                kh.setEmail(email);
                khachHangService.save(kh);

                // Tạo tài khoản mới
                TaiKhoan tk = new TaiKhoan();
                tk.setTenTK(email);
                tk.setKhachHang(kh);
                taiKhoanService.save(tk);
            }
            return oAuth2User;
        };
    }
}