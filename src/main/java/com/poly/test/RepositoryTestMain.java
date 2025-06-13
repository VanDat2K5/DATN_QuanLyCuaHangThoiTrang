package com.poly.test;

import com.poly.entity.ChiTietHoaDon;
import com.poly.service.ChiTietHoaDonService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication(scanBasePackages = "com.poly")
public class RepositoryTestMain {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RepositoryTestMain.class, args);
        ChiTietHoaDonService chiTietHoaDonService = context.getBean(ChiTietHoaDonService.class);

        String maHD = "HD001";
        List<ChiTietHoaDon> ds = chiTietHoaDonService.findByHoaDon_MaHD(maHD);

        System.out.println("===== THÔNG TIN CHI TIẾT HÓA ĐƠN: " + maHD + " =====");
        for (ChiTietHoaDon cthd : ds) {
            System.out.println("Tên SP: " + cthd.getChiTietSanPham().getSanPham().getTenSP());
            System.out.println("Màu: " + cthd.getChiTietSanPham().getMau().getTenMau());
            System.out.println("Size: " + cthd.getChiTietSanPham().getSize().getTenSize());
            System.out.println("Số lượng: " + cthd.getSoLuongXuat());
            System.out.println("Giá xuất: " + cthd.getGiaXuat());
            System.out.println("Thành tiền: " + cthd.getThanhTien());
            System.out.println("-----------------------------");
        }

        if (ds.isEmpty()) {
            System.out.println("=> Không tìm thấy hóa đơn!");
        }

        SpringApplication.exit(context);
    }
}
