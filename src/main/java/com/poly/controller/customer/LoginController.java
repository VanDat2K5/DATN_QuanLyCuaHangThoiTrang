package com.poly.controller.customer;

import com.poly.entity.KhachHang;
import com.poly.entity.TaiKhoan;
import com.poly.service.KhachHangService;
import com.poly.service.TaiKhoanService;
import com.poly.util.CodeGenerator;
import com.poly.util.EmailService;
import com.poly.util.PasswordResetTokenUtil;
import com.poly.util.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private ResetPasswordLimiter resetPasswordLimiter;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        // Nếu đã đăng nhập, chuyển hướng về trang chủ
        if (Security.isAuthenticated(session)) {
            String userRole = Security.getUserRole(session);
            if ("CUSTOMER".equals(userRole)) {
                return "redirect:/";
            } else if ("ADMIN".equals(userRole)) {
                return "redirect:/";
            }
        }
        System.out.println("==> Đã vào controller GET /login");
        return "Client/demo-fashion-store-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String usernameOrEmail,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {
        System.out.println("==> Đã vào controller POST /login");

        // Kiểm tra xem input là username hay email
        boolean isEmail = usernameOrEmail.contains("@");

        Optional<TaiKhoan> taiKhoanOpt;

        if (isEmail) {
            // Nếu là email, tìm theo email của khách hàng
            Optional<KhachHang> khachHangOpt = khachHangService.findByEmail(usernameOrEmail);
            if (khachHangOpt.isPresent()) {
                // Tìm tài khoản liên kết với khách hàng này
                taiKhoanOpt = taiKhoanService.findByKhachHangAndMatKhau(khachHangOpt.get(), password);
            } else {
                taiKhoanOpt = Optional.empty();
            }
        } else {
            // Nếu là username, tìm theo tên tài khoản
            taiKhoanOpt = taiKhoanService.findByTenTKAndMatKhau(usernameOrEmail, password);
        }

        if (taiKhoanOpt.isPresent()) {
            TaiKhoan taiKhoan = taiKhoanOpt.get();

            if (taiKhoan.getKhachHang() != null) {
                // Đăng nhập thành công cho khách hàng
                session.setAttribute("user", taiKhoan.getKhachHang());
                session.setAttribute("userRole", "CUSTOMER");
                session.setAttribute("username", taiKhoan.getTenTK());
                redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
                return "redirect:/";
            } else if (taiKhoan.getNhanVien() != null) {
                // Đăng nhập thành công cho nhân viên/admin
                session.setAttribute("user", taiKhoan.getNhanVien());
                if (taiKhoan.getNhanVien().getIsAdmin()) {
                    session.setAttribute("userRole", "ADMIN");
                } else {
                    session.setAttribute("userRole", "EMPLOYEE");
                }
                session.setAttribute("username", taiKhoan.getTenTK());
                return "redirect:/admin/";
            } else {
                model.addAttribute("error", "Tài khoản không hợp lệ.");
                return "Client/demo-fashion-store-login";
            }
        } else {
            model.addAttribute("error", "Tên đăng nhập/Email hoặc mật khẩu không đúng.");
            return "Client/demo-fashion-store-login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(HttpSession session) {
        if (Security.isAuthenticated(session)) {
            return "redirect:/";
        }
        return "Client/demo-fashion-store-register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            RedirectAttributes redirectAttributes) {

        // Validation
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "redirect:/register";
        }

        if (taiKhoanService.findByTenTK(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại.");
            return "redirect:/register";
        }

        // Kiểm tra email đã tồn tại chưa
        if (khachHangService.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
            return "redirect:/register";
        }

        try {
            KhachHang khachHang = new KhachHang();
            // Sử dụng CustomerCodeGenerator để tạo mã khách hàng
            String maKH = codeGenerator.generateCustomerCode();
            khachHang.setMaKH(maKH);

            khachHang.setTenKH(fullname);
            khachHang.setEmail(email);
            khachHang.setSoDT(phone);

            khachHangService.save(khachHang);

            // Tạo tài khoản mới
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenTK(username);
            taiKhoan.setMatKhau(password);
            taiKhoan.setKhachHang(khachHang);

            taiKhoanService.save(taiKhoan);

            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra trong quá trình đăng ký.");
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa tất cả thông tin session
        session.removeAttribute("user");
        session.removeAttribute("userRole");
        session.removeAttribute("username");
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "Client/demo-fashion-store-access-denied";
    }

    // Phương thức kiểm tra quyền truy cập
    public boolean checkAccess(HttpServletRequest request, HttpServletResponse response, String requiredRole) {
        try {
            Security.handleAccessControl(request, response, requiredRole);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Trang quên mật khẩu
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(HttpSession session) {
        if (Security.isAuthenticated(session)) {
            return "redirect:/";
        }
        return "Client/demo-fashion-store-forgot-password";
    }

    @PostMapping("/forgot-password/send")
    public String sendForgotPasswordEmail(@RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra giới hạn gửi email reset password
            if (!resetPasswordLimiter.canSend(email)) {
                redirectAttributes.addFlashAttribute("error",
                        "Bạn đã gửi quá số lần cho phép. Vui lòng thử lại sau 1 phút.");
                return "redirect:/forgot-password";
            }
            // Kiểm tra email có tồn tại không
            Optional<KhachHang> khachHangOpt = khachHangService.findByEmail(email);
            if (khachHangOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email không tồn tại trong hệ thống.");
                return "redirect:/forgot-password";
            }

            KhachHang khachHang = khachHangOpt.get();

            // Tạo token và gửi email
            String resetToken = PasswordResetTokenUtil.generateToken(email);
            emailService.sendPasswordResetEmail(khachHang, resetToken);

            redirectAttributes.addFlashAttribute("success",
                    "Email đã được gửi đến " + email + ". Vui lòng kiểm tra hộp thư.");

        } catch (Exception e) {
            System.err.println("Lỗi gửi email: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
        }

        return "redirect:/forgot-password";
    }

    // Trang đặt lại mật khẩu
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token,
            Model model) {
        if (!PasswordResetTokenUtil.validateToken(token)) {
            model.addAttribute("error", "Link không hợp lệ hoặc đã hết hạn.");
            return "Client/demo-fashion-store-reset-password";
        }

        model.addAttribute("token", token);
        return "Client/demo-fashion-store-reset-password";
    }

    // Xử lý đặt lại mật khẩu
    @PostMapping("/reset-password/update")
    public String updatePassword(@RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra token
        if (!PasswordResetTokenUtil.validateToken(token)) {
            redirectAttributes.addFlashAttribute("error", "Token không hợp lệ.");
            return "redirect:/login";
        }

        // Kiểm tra mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "redirect:/reset-password?token=" + token;
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự.");
            return "redirect:/reset-password?token=" + token;
        }

        try {
            // Lấy email từ token
            String email = PasswordResetTokenUtil.getEmailFromToken(token);
            if (email == null) {
                redirectAttributes.addFlashAttribute("error", "Không thể xác định email.");
                return "redirect:/login";
            }

            // Tìm khách hàng và tài khoản
            Optional<KhachHang> khachHangOpt = khachHangService.findByEmail(email);
            if (khachHangOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản.");
                return "redirect:/login";
            }

            KhachHang khachHang = khachHangOpt.get();
            Optional<TaiKhoan> taiKhoanOpt = taiKhoanService.findByKhachHang(khachHang);
            if (taiKhoanOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản đăng nhập.");
                return "redirect:/login";
            }

            // Cập nhật mật khẩu
            TaiKhoan taiKhoan = taiKhoanOpt.get();
            taiKhoan.setMatKhau(newPassword);
            taiKhoanService.save(taiKhoan);

            redirectAttributes.addFlashAttribute("success", "Đặt lại mật khẩu thành công!");

        } catch (Exception e) {
            System.err.println("Lỗi cập nhật mật khẩu: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
            return "redirect:/reset-password?token=" + token;
        }

        return "redirect:/login";
    }
}