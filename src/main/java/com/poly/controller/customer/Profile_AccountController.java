package com.poly.controller.customer;

import com.poly.entity.KhachHang;
import com.poly.entity.TaiKhoan;
import com.poly.service.TaiKhoanService;
import com.poly.util.Security;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class Profile_AccountController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @PostMapping("/profile/create-account")
    public String createAccount(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Validation
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/user/profile";
        }

        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/user/profile";
        }

        // Check if username already exists
        if (taiKhoanService.findByTenTK(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/user/profile";
        }

        try {
            String userRole = Security.getUserRole(session);
            if (!"CUSTOMER".equals(userRole)) {
                redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể tạo tài khoản!");
                return "redirect:/user/profile";
            }

            KhachHang khachHang = (KhachHang) session.getAttribute("user");

            // Check if customer already has an account
            Optional<TaiKhoan> existingAccount = taiKhoanService.findByKhachHang(khachHang);
            if (existingAccount.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Tài khoản đã tồn tại!");
                return "redirect:/user/profile";
            }

            // Create new account
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenTK(username);
            taiKhoan.setMatKhau(password);
            taiKhoan.setKhachHang(khachHang);

            taiKhoanService.save(taiKhoan);

            // Update session - remove OAuth flags
            session.setAttribute("username", username);
            session.removeAttribute("needsAccount");
            session.removeAttribute("oauthUser");

            redirectAttributes.addFlashAttribute("success", "Tạo tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo tài khoản: " + e.getMessage());
        }

        return "redirect:/user/profile";
    }
} 