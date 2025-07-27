package com.poly.controller.customer;

import com.poly.entity.KhachHang;
import com.poly.entity.NhanVien;
import com.poly.service.KhachHangService;
import com.poly.service.NhanVienService;
import com.poly.util.Security;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class Profile_EditController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienService nhanVienService;

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            Object user = session.getAttribute("user");

            if ("CUSTOMER".equals(userRole)) {
                KhachHang khachHang = (KhachHang) user;
                khachHang.setTenKH(fullName);
                khachHang.setEmail(email);
                khachHang.setSoDT(phone);
                khachHangService.save(khachHang);
                session.setAttribute("user", khachHang);
            } else {
                NhanVien nhanVien = (NhanVien) user;
                nhanVien.setTenNV(fullName);
                nhanVien.setEmail(email);
                nhanVien.setSoDT(phone);
                nhanVienService.save(nhanVien);
                session.setAttribute("user", nhanVien);
            }

            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
        }

        return "redirect:/user/profile";
    }
} 