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
import java.util.List;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TaiKhoanService taiKhoanService;
    private final KhachHangService khachHangService;

    public CustomOAuth2SuccessHandler(TaiKhoanService taiKhoanService, KhachHangService khachHangService) {
        this.taiKhoanService = taiKhoanService;
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

        // Nếu chưa có tài khoản, tạo mới
        System.out.println("==> Tạo tài khoản mới cho Google: " + email);

        // Tạo khách hàng mới với mã tự tăng
        KhachHang kh = new KhachHang();
        List<KhachHang> allKhachHang = khachHangService.findAll();
        int nextNumber = allKhachHang.size() + 1;
        String maKH = String.format("KH%03d", nextNumber); // KH001, KH002,...
        kh.setMaKH(maKH);
        kh.setTenKH(oAuth2User.getAttribute("name"));
        kh.setEmail(email);
        khachHangService.save(kh);

        // Tạo tài khoản mới
        TaiKhoan tk = new TaiKhoan();
        tk.setTenTK(email);
        tk.setKhachHang(kh);
        taiKhoanService.save(tk); // Lưu tài khoản

        // Set session và chuyển hướng
        HttpSession session = request.getSession();
        session.setAttribute("user", kh);
        session.setAttribute("userRole", "CUSTOMER");
        session.setAttribute("username", email);
        System.out.println("==> Đã tạo và set session cho user Google: " + email);
        response.sendRedirect("/");
    }
}