package com.poly.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        // Sử dụng Security để kiểm tra quyền admin
        if (!Security.isAuthenticated(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        if (!Security.hasStaffAccess(session)) {
            response.sendRedirect(request.getContextPath() + "/access-denied");
            return false;
        }
        return true;
    }

}
