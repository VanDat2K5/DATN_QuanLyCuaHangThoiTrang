package com.poly.util;

import com.poly.entity.KhachHang;
import com.poly.entity.NhanVien;
import com.poly.entity.PhieuNhap;
import com.poly.entity.SanPham;
import com.poly.service.HoaDonService;
import com.poly.service.KhachHangService;
import com.poly.service.PhieuNhapService;
import com.poly.service.NhanVienService;
import com.poly.entity.HoaDon;
import com.poly.service.SanPhamService;
import com.poly.entity.ChiTietPhieuNhap;
import com.poly.service.ChiTietPhieuNhapService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CodeGenerator {

    private final KhachHangService khachHangService;
    private final NhanVienService nhanVienService;
    private final HoaDonService hoaDonService;
    private final PhieuNhapService phieuNhapService;
    private final SanPhamService sanPhamService;
    private final ChiTietPhieuNhapService chiTietPhieuNhapService;

    @Autowired
    public CodeGenerator(KhachHangService khachHangService, NhanVienService nhanVienService,
            HoaDonService hoaDonService, PhieuNhapService phieuNhapService, SanPhamService sanPhamService,
            ChiTietPhieuNhapService chiTietPhieuNhapService) {
        this.khachHangService = khachHangService;
        this.nhanVienService = nhanVienService;
        this.hoaDonService = hoaDonService;
        this.phieuNhapService = phieuNhapService;
        this.sanPhamService = sanPhamService;
        this.chiTietPhieuNhapService = chiTietPhieuNhapService;
    }

    public String generateCustomerCode() {
        List<KhachHang> allKhachHang = khachHangService.findAll();
        int nextNumber = allKhachHang.size() + 1;
        return String.format("KH%03d", nextNumber);
    }

    public String generateProductCode() {
        List<SanPham> allSanPham = sanPhamService.findAll();
        int nextNumber = allSanPham.size() + 1;
        return String.format("SP%03d", nextNumber);
    }

    public String generateCustomerCode(String prefix) {
        List<KhachHang> allKhachHang = khachHangService.findAll();
        int nextNumber = allKhachHang.size() + 1;
        return String.format("%s%03d", prefix, nextNumber);
    }

    public String generateEmployeeCode() {
        List<NhanVien> allNhanVien = nhanVienService.findAll();
        int nextNumber = allNhanVien.size() + 1;
        return String.format("NV%03d", nextNumber);
    }

    public String generateOrderCode() {
        List<HoaDon> allHoaDon = hoaDonService.findAll();
        int nextNumber = allHoaDon.size() + 1;
        return String.format("HD%03d", nextNumber);
    }

    public String generateImportCode() {
        List<PhieuNhap> allPhieuNhap = phieuNhapService.findAll();
        int nextNumber = allPhieuNhap.size() + 1;
        return String.format("PN%03d", nextNumber);
    }

    /**
     * Sinh mã lô hàng cho chi tiết phiếu nhập.
     * Nếu sản phẩm, màu, size đã tồn tại trong các chi tiết phiếu nhập trước đó,
     * thì tăng số lô hàng lên (ví dụ: LO1, LO2, ... cho cùng một sản
     * phẩm/màu/size).
     * Nếu chưa có thì trả về LO1.
     */
    public String generateImportDetailCode(ChiTietPhieuNhap chiTietMoi) {
        List<ChiTietPhieuNhap> allChiTietPhieuNhap = chiTietPhieuNhapService.findAll();

        int maxLo = 0;
        for (ChiTietPhieuNhap ct : allChiTietPhieuNhap) {
            if (ct.getChiTietSanPham() != null && chiTietMoi.getChiTietSanPham() != null) {
                boolean sameSP = ct.getChiTietSanPham().getSanPham() != null
                        && chiTietMoi.getChiTietSanPham().getSanPham() != null
                        && ct.getChiTietSanPham().getSanPham().getMaSP()
                                .equals(chiTietMoi.getChiTietSanPham().getSanPham().getMaSP());
                boolean sameMau = ct.getChiTietSanPham().getMau() != null
                        && chiTietMoi.getChiTietSanPham().getMau() != null
                        && ct.getChiTietSanPham().getMau().getMaMau()
                                .equals(chiTietMoi.getChiTietSanPham().getMau().getMaMau());
                boolean sameSize = ct.getChiTietSanPham().getSize() != null
                        && chiTietMoi.getChiTietSanPham().getSize() != null
                        && ct.getChiTietSanPham().getSize().getMaSize()
                                .equals(chiTietMoi.getChiTietSanPham().getSize().getMaSize());
                if (sameSP && sameMau && sameSize) {
                    String loHang = ct.getLoHang();
                    if (loHang != null && loHang.startsWith("LO")) {
                        try {
                            int soLo = Integer.parseInt(loHang.substring(2));
                            if (soLo > maxLo) {
                                maxLo = soLo;
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        int nextLo = maxLo + 1;
        return "LO" + nextLo;
    }

    // khachHang.setMaKH(UUID.randomUUID().toString().substring(0, 20));
    // public boolean isCustomerCodeExists(String maKH) {
    // List<KhachHang> allKhachHang = khachHangService.findAll();
    // return allKhachHang.stream()
    // .anyMatch(kh -> maKH.equals(kh.getMaKH()));
    // }
}