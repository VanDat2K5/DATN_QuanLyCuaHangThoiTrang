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
import com.poly.entity.ChiTietSanPham;
import com.poly.service.ChiTietSanPhamService;

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
    private final ChiTietSanPhamService chiTietSanPhamService;

    @Autowired
    public CodeGenerator(KhachHangService khachHangService, NhanVienService nhanVienService,
            HoaDonService hoaDonService, PhieuNhapService phieuNhapService, SanPhamService sanPhamService,
            ChiTietSanPhamService chiTietSanPhamService) {
        this.khachHangService = khachHangService;
        this.nhanVienService = nhanVienService;
        this.hoaDonService = hoaDonService;
        this.phieuNhapService = phieuNhapService;
        this.sanPhamService = sanPhamService;
        this.chiTietSanPhamService = chiTietSanPhamService;
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

    public String generateProductDetailCode(String maSP, String mau, String size) {
        List<ChiTietSanPham> allChiTietSanPham = chiTietSanPhamService.findAll();
        int maxLo = 0;
        for (ChiTietSanPham ct : allChiTietSanPham) {
            if (ct.getSanPham().getMaSP().equals(maSP) && ct.getMau().getMaMau().equals(mau)
                    && ct.getSize().getMaSize().equals(size)) {
                String loHang = ct.getLoHang();
                if (loHang != null && loHang.startsWith("LH")) {
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
        return maSP + "_" + mau + "_" + size + "_" + "LH" + (maxLo + 1);
    }

    public String generateImportDetailCode(String maSP, String mau, String size) {
        List<ChiTietSanPham> allChiTietSanPham = chiTietSanPhamService.findAll();
        int maxLo = 0;
        for (ChiTietSanPham ct : allChiTietSanPham) {
            if (ct.getSanPham().getMaSP().equals(maSP) && ct.getMau().getMaMau().equals(mau)
                    && ct.getSize().getMaSize().equals(size)) {
                String loHang = ct.getLoHang();
                if (loHang != null && loHang.startsWith("LH")) {
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
        return "LH" + (maxLo + 1);
    }

    // khachHang.setMaKH(UUID.randomUUID().toString().substring(0, 20));
    // public boolean isCustomerCodeExists(String maKH) {
    // List<KhachHang> allKhachHang = khachHangService.findAll();
    // return allKhachHang.stream()
    // .anyMatch(kh -> maKH.equals(kh.getMaKH()));
    // }
}