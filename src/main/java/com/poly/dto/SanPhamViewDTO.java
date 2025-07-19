package com.poly.dto;

import com.poly.entity.SanPham;

import java.math.BigDecimal;

public class SanPhamViewDTO {
    private String maSP;
    private String tenSP;
    private String hinhAnhDauTien;
    private BigDecimal giaXuat;

    public SanPhamViewDTO(SanPham sp) {
        this.maSP = sp.getMaSP();
        this.tenSP = sp.getTenSP();

        if (sp.getHinhAnh() != null && !sp.getHinhAnh().isEmpty()) {
            this.hinhAnhDauTien = "/images/" + sp.getHinhAnh().get(0).getHinhAnh();
        }

        if (sp.getChiTietSanPham() != null && !sp.getChiTietSanPham().isEmpty()) {
            this.giaXuat = sp.getChiTietSanPham().get(0).getGiaXuat();
        }
    }

    // Getters and Setters
    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getHinhAnhDauTien() {
        return hinhAnhDauTien;
    }

    public void setHinhAnhDauTien(String hinhAnhDauTien) {
        this.hinhAnhDauTien = hinhAnhDauTien;
    }

    public BigDecimal getGiaXuat() {
        return giaXuat;
    }

    public void setGiaXuat(BigDecimal giaXuat) {
        this.giaXuat = giaXuat;
    }
}
