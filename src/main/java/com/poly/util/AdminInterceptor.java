package com.poly.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.poly.entity.TaiKhoan;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user != null) {
            if (user.getNhanVien() != null) {
                session.setAttribute("isEmployee", true);
                session.setAttribute("isCustomer", false);
                if (user.getNhanVien().getIsAdmin()) {
                    session.setAttribute("isAdmin", true);
                }
            } else if (user.getKhachHang() != null) {
                session.setAttribute("isEmployee", false);
                session.setAttribute("isCustomer", true);
            }
        }
        if (user == null || !Boolean.TRUE.equals(user.getNhanVien().getIsAdmin())) {
            response.sendRedirect("/account/sign-in");
            return false;
        }

        return true;
    }

}
