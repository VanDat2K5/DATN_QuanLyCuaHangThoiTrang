package com.poly.util;

import com.poly.entity.KhachHang;
import com.poly.entity.NhanVien;
import com.poly.service.KhachHangService;
import com.poly.service.NhanVienService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CodeGenerator {

    private final KhachHangService khachHangService;
    private final NhanVienService nhanVienService;

    @Autowired
    public CodeGenerator(KhachHangService khachHangService, NhanVienService nhanVienService) {
        this.khachHangService = khachHangService;
        this.nhanVienService = nhanVienService;
    }

    public String generateCustomerCode() {
        List<KhachHang> allKhachHang = khachHangService.findAll();
        int nextNumber = allKhachHang.size() + 1;
        return String.format("KH%03d", nextNumber);
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

    // khachHang.setMaKH(UUID.randomUUID().toString().substring(0, 20));
    // public boolean isCustomerCodeExists(String maKH) {
    // List<KhachHang> allKhachHang = khachHangService.findAll();
    // return allKhachHang.stream()
    // .anyMatch(kh -> maKH.equals(kh.getMaKH()));
    // }
}