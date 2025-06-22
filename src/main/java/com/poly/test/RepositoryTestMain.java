package com.poly.test;

import com.poly.entity.ChiTietHoaDon;
import com.poly.entity.HoaDon;
import com.poly.entity.KhachHang;
import com.poly.service.ChiTietHoaDonService;
import com.poly.service.HoaDonService;
import com.poly.service.KhachHangService;
import com.poly.util.EmailService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

@SpringBootApplication(scanBasePackages = "com.poly")
public class RepositoryTestMain {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RepositoryTestMain.class, args);

        // Lấy các service cần thiết
        EmailService emailService = context.getBean(EmailService.class);
        HoaDonService hoaDonService = context.getBean(HoaDonService.class);
        KhachHangService khachHangService = context.getBean(KhachHangService.class);
        ChiTietHoaDonService chiTietHoaDonService = context.getBean(ChiTietHoaDonService.class);

        try {
            // Test 1: Gửi email xác nhận đơn hàng
            System.out.println("===== TEST GỬI EMAIL XÁC NHẬN ĐƠN HÀNG =====");

            String maHD = "HD002";
            Optional<HoaDon> hoaDonOpt = hoaDonService.findById(maHD);

            if (hoaDonOpt.isPresent()) {
                HoaDon hoaDon = hoaDonOpt.get();
                KhachHang khachHang = hoaDon.getKhachHang();

                System.out.println("Thông tin hóa đơn:");
                System.out.println("- Mã HD: " + hoaDon.getMaHD());
                System.out.println("- Khách hàng: " + khachHang.getTenKH());
                System.out.println("- Email: " + khachHang.getEmail());
                System.out.println("- Tổng tiền: " + hoaDon.getTongTien());
                System.out.println("- Ngày lập: " + hoaDon.getNgayLap());

                // Kiểm tra chi tiết hóa đơn
                List<ChiTietHoaDon> chiTietList = chiTietHoaDonService.findByHoaDon_MaHD(maHD);
                System.out.println("- Số sản phẩm: " + chiTietList.size());

                // Gửi email
                System.out.println("\nĐang gửi email...");
                emailService.sendOrderConfirmation(khachHang, hoaDon);
                System.out.println("✅ Email đã được gửi thành công!");

            } else {
                System.out.println("❌ Không tìm thấy hóa đơn với mã: " + maHD);
            }

            // Test 2: Gửi email reset password
            System.out.println("\n===== TEST GỬI EMAIL RESET PASSWORD =====");

            // Lấy khách hàng test
            Optional<KhachHang> testKhachHangOpt = khachHangService.findByEmail("vandat05022005@gmail.com");
            KhachHang testKhachHang;

            testKhachHang = testKhachHangOpt.get();

            String resetToken = "test-reset-token-123";
            System.out.println("Đang gửi email reset password...");
            emailService.sendPasswordResetEmail(testKhachHang, resetToken);
            System.out.println("✅ Email reset password đã được gửi thành công!");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi test email: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n===== HOÀN THÀNH TEST EMAIL =====");
        SpringApplication.exit(context);
    }
}