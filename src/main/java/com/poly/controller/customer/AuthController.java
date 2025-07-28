package com.poly.controller.customer;

import com.poly.entity.ChiTietHoaDon;
import com.poly.entity.DiaChiNhanHang;
import com.poly.entity.HinhAnh;
import com.poly.entity.HoaDon;
import com.poly.entity.KhachHang;
import com.poly.entity.NhanVien;
import com.poly.entity.TaiKhoan;
import com.poly.service.HoaDonService;
import com.poly.service.KhachHangService;
import com.poly.service.NhanVienService;
import com.poly.service.TaiKhoanService;
import com.poly.service.ChiTietHoaDonService;
import com.poly.service.DiaChiNhanHangService;
import com.poly.service.HinhAnhService;
import com.poly.util.Security;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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

    @Autowired
    private DiaChiNhanHangService diaChiNhanHangService;

    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;

    @Autowired
    private HinhAnhService hinhAnhService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        String userRole = Security.getUserRole(session);
        Object user = session.getAttribute("user");

        // Add common user data
        model.addAttribute("user", user);
        model.addAttribute("userRole", userRole);
        model.addAttribute("username", session.getAttribute("username"));

        // Add OAuth specific attributes with default values
        Boolean oauthUser = (Boolean) session.getAttribute("oauthUser");
        Boolean needsAccount = (Boolean) session.getAttribute("needsAccount");
        model.addAttribute("oauthUser", oauthUser != null ? oauthUser : false);
        model.addAttribute("needsAccount", needsAccount != null ? needsAccount : false);

        // Get order statistics and pagination based on user type
        Page<HoaDon> ordersPage;
        List<HoaDon> allUserOrders;

        Pageable pageable = PageRequest.of(page, size);

        if (user != null && "CUSTOMER".equals(userRole)) {
            KhachHang khachHang = (KhachHang) user;

            // Get paginated orders for customer
            ordersPage = hoaDonService.findByKhachHangOrderByNgayLapDesc(khachHang.getMaKH(), pageable);
            allUserOrders = hoaDonService.findByKhachHang_MaKH(khachHang.getMaKH());

            // Get shipping addresses for customer
            List<DiaChiNhanHang> diaChiList = diaChiNhanHangService
                    .findByKhachHang_MaKH(khachHang.getMaKH());
            model.addAttribute("diaChiList", diaChiList);

        } else if (user != null && !"CUSTOMER".equals(userRole)) {
            NhanVien nhanVien = (NhanVien) user;

            // Get paginated orders for employee using the new service method
            ordersPage = hoaDonService.findByNhanVienOrderByNgayLapDesc(nhanVien.getMaNV(), pageable);
            allUserOrders = hoaDonService.findByNhanVien_MaNV(nhanVien.getMaNV());
        } else {
            ordersPage = Page.empty(pageable);
            allUserOrders = List.of();
        }

        // Common statistics calculation
        model.addAttribute("totalOrders", allUserOrders.size());

        BigDecimal totalAmount = allUserOrders.stream()
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalSpent", totalAmount);

        // Count orders by status
        Map<String, Long> ordersByStatus = allUserOrders.stream()
                .collect(Collectors.groupingBy(HoaDon::getTrangThai, Collectors.counting()));
        model.addAttribute("ordersByStatus", ordersByStatus);

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "Client/demo-fashion-store-profile";
    }

    @GetMapping("/order/{orderId}")
    public String viewOrderDetails(@PathVariable String orderId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        String userRole = Security.getUserRole(session);
        Object user = session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem chi tiết đơn hàng");
            return "redirect:/user/profile";
        }

        // Find order by ID
        Optional<HoaDon> orderOpt = hoaDonService.findById(orderId);
        if (!orderOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/user/profile";
        }

        HoaDon order = orderOpt.get();

        // Check if user owns this order (for customers) or has permission (for
        // employees)
        if ("CUSTOMER".equals(userRole)) {
            KhachHang customer = (KhachHang) user;
            if (!order.getKhachHang().getMaKH().equals(customer.getMaKH())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xem đơn hàng này");
                return "redirect:/user/profile";
            }
        }

        // Get order details
        List<ChiTietHoaDon> orderDetails = chiTietHoaDonService.findByHoaDon_MaHD(orderId);
        for (ChiTietHoaDon cthd : orderDetails) {
            List<HinhAnh> hinhAnh = hinhAnhService.findBySanPham_MaSP(cthd.getChiTietSanPham().getSanPham().getMaSP());
            cthd.getChiTietSanPham().getSanPham().setHinhAnh(hinhAnh);
        }

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("userRole", userRole);
        model.addAttribute("user", user);

        return "Client/order-details";
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

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/user/profile";
        }

        try {
            String userRole = Security.getUserRole(session);
            Object user = session.getAttribute("user");

            if ("CUSTOMER".equals(userRole)) {
                KhachHang khachHang = (KhachHang) user;
                Optional<TaiKhoan> account = taiKhoanService.findByKhachHang(khachHang);

                if (account.isPresent()) {
                    TaiKhoan taiKhoan = account.get();
                    if (!taiKhoan.getMatKhau().equals(currentPassword)) {
                        redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
                        return "redirect:/user/profile";
                    }

                    taiKhoan.setMatKhau(newPassword);
                    taiKhoanService.save(taiKhoan);
                } else {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản!");
                    return "redirect:/user/profile";
                }
            } else {
                // For employees, you might need to implement password change logic
                redirectAttributes.addFlashAttribute("error", "Chức năng đổi mật khẩu chưa hỗ trợ cho nhân viên!");
                return "redirect:/user/profile";
            }

            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi đổi mật khẩu: " + e.getMessage());
        }

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
            HoaDon order = hoaDonService.findById(orderId).orElse(null);
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
