package com.poly.controller.customer;

import com.poly.entity.KhachHang;
import com.poly.entity.NhanVien;
import com.poly.entity.TaiKhoan;
import com.poly.service.HoaDonService;
import com.poly.service.KhachHangService;
import com.poly.service.NhanVienService;
import com.poly.service.TaiKhoanService;
import com.poly.util.Security;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        String userRole = Security.getUserRole(session);
        Object user = session.getAttribute("user");

        // Debug logging
        System.out.println("=== Profile Debug ===");
        System.out.println("user: " + (user != null ? user.getClass().getSimpleName() : "null"));
        System.out.println("userRole: " + userRole);
        System.out.println("username: " + session.getAttribute("username"));

        // Add common user data
        model.addAttribute("user", user);
        model.addAttribute("userRole", userRole);
        model.addAttribute("username", session.getAttribute("username"));

        // Add OAuth specific attributes
        Boolean oauthUser = (Boolean) session.getAttribute("oauthUser");
        Boolean needsAccount = (Boolean) session.getAttribute("needsAccount");

        // Set default values if null
        model.addAttribute("oauthUser", oauthUser != null ? oauthUser : false);
        model.addAttribute("needsAccount", needsAccount != null ? needsAccount : false);

        // Get order statistics based on user type
        List<com.poly.entity.HoaDon> userOrders = new ArrayList<>();
        if (user != null && "CUSTOMER".equals(userRole)) {
            KhachHang khachHang = (KhachHang) user;
            userOrders = hoaDonService.findByKhachHang_MaKH(khachHang.getMaKH());
        } else if (user != null && !"CUSTOMER".equals(userRole)) {
            NhanVien nhanVien = (NhanVien) user;
            userOrders = hoaDonService.findByNhanVien_MaNV(nhanVien.getMaNV());
        }

        // Common statistics calculation
        model.addAttribute("totalOrders", userOrders.size());

        BigDecimal totalAmount = userOrders.stream()
                .map(com.poly.entity.HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalSpent", totalAmount);

        // Count orders by status
        Map<String, Long> ordersByStatus = userOrders.stream()
                .collect(Collectors.groupingBy(com.poly.entity.HoaDon::getTrangThai, Collectors.counting()));
        model.addAttribute("ordersByStatus", ordersByStatus);

        // Get recent orders (last 5)
        List<com.poly.entity.HoaDon> recentOrders = userOrders.stream()
                .sorted((o1, o2) -> o2.getNgayLap().compareTo(o1.getNgayLap()))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("recentOrders", recentOrders);

        return "Client/demo-fashion-store-profile";
    }

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

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp!");
            return "redirect:/user/profile";
        }

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/user/profile";
    }

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

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            String userRole = Security.getUserRole(session);
            Object user = session.getAttribute("user");

            // Find order by ID
            com.poly.entity.HoaDon order = hoaDonService.findById(orderId).orElse(null);
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
                return "redirect:/user/profile";
            }

            // Check if user owns this order (for customers) or has permission (for
            // employees)
            if ("CUSTOMER".equals(userRole)) {
                KhachHang customer = (KhachHang) user;
                if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
                    redirectAttributes.addFlashAttribute("error", "Bạn không có quyền hủy đơn hàng này");
                    return "redirect:/user/profile";
                }
            }

            // Check if order can be cancelled
            if (!order.getTrangThai().equals("ChoXacNhan") && !order.getTrangThai().equals("DaThanhToan")) {
                redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng ở trạng thái này");
                return "redirect:/user/profile";
            }

            // Update order status to cancelled
            order.setTrangThai("DaHuy");
            hoaDonService.save(order);

            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi hủy đơn hàng");
        }

        return "redirect:/user/profile";
    }
}
