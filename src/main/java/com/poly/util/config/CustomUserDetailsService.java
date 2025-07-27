package com.poly.util.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Đây là một implementation đơn giản
        // Trong thực tế, bạn sẽ load user từ database
        // Vì chúng ta đang sử dụng session-based authentication, 
        // method này có thể không được sử dụng nhiều
        throw new UsernameNotFoundException("User not found: " + username);
    }
} 