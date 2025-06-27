package com.poly.config;

import com.poly.entity.KhachHang;
import com.poly.entity.TaiKhoan;
import com.poly.service.KhachHangService;
import com.poly.service.TaiKhoanService;
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

    // private final TaiKhoanService taiKhoanService;
    private final KhachHangService khachHangService;

    public CustomOAuth2SuccessHandler(TaiKhoanService taiKhoanService, KhachHangService khachHangService) {
        // this.taiKhoanService = taiKhoanService;
        this.khachHangService = khachHangService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        System.out.println("==> Đã vào CustomOAuth2SuccessHandler, email: " + email);

        Optional<KhachHang> khOpt = khachHangService.findByEmail(email);
        if (khOpt.isPresent()) {
            TaiKhoan tk = khOpt.get().getTaiKhoan();
            if (tk != null) {
                KhachHang kh = tk.getKhachHang();
                HttpSession session = request.getSession();
                session.setAttribute("user", kh);
                session.setAttribute("userRole", "CUSTOMER");
                session.setAttribute("username", email);
                System.out.println("==> Đã set session cho user Google: " + email);
                response.sendRedirect("/");
                return;
            }
        }
        // Nếu chưa có tài khoản, chuyển sang trang đăng ký và truyền sẵn email
        System.out.println("==> Tài khoản Google chưa tồn tại, chuyển sang đăng ký: " + email);
        response.sendRedirect("/register?email=" + java.net.URLEncoder.encode(email, "UTF-8"));
    }
}