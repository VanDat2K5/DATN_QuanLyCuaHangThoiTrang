package com.poly.util;

import com.poly.entity.KhachHang;
import com.poly.service.KhachHangService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final KhachHangService khachHangService;
    private final CodeGenerator codeGenerator;

    public CustomOAuth2SuccessHandler(KhachHangService khachHangService,
            CodeGenerator codeGenerator) {
        this.khachHangService = khachHangService;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        System.out.println("==> Đã vào CustomOAuth2SuccessHandler, email: " + email);

        Optional<KhachHang> khOpt = khachHangService.findByEmail(email);
        if (khOpt.isPresent()) {
            // Khách hàng đã tồn tại
            KhachHang kh = khOpt.get();
            HttpSession session = request.getSession();
            session.setAttribute("user", kh);
            session.setAttribute("userRole", "CUSTOMER");
            session.setAttribute("username", email);
            session.setAttribute("oauthUser", true);
            if (kh.getTaiKhoan() == null) {
                session.setAttribute("needsAccount", true);
            }
            System.out.println("==> Đã set session cho user Google: " + email);
            response.sendRedirect("/");
            return;
        }

        // Nếu chưa có khách hàng, tạo mới
        System.out.println("==> Tạo khách hàng mới cho Google: " + email);

        // Tạo khách hàng mới với mã tự tăng
        KhachHang kh = new KhachHang();
        String maKH = codeGenerator.generateCustomerCode();

        kh.setMaKH(maKH);
        kh.setTenKH(oAuth2User.getAttribute("name"));
        kh.setSoDT(oAuth2User.getAttribute("phone"));
        kh.setEmail(email);
        khachHangService.save(kh);

        // Set session và chuyển hướng (không tạo tài khoản)
        HttpSession session = request.getSession();
        session.setAttribute("user", kh);
        session.setAttribute("userRole", "CUSTOMER");
        session.setAttribute("username", email);
        session.setAttribute("oauthUser", true); // Đánh dấu là OAuth2 user
        session.setAttribute("needsAccount", true); // Đánh dấu cần tạo tài khoản
        System.out.println("==> Đã tạo khách hàng và set session cho user Google: " + email);
        response.sendRedirect("/");
    }
}