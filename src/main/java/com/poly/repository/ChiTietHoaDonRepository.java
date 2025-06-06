package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.ChiTietHoaDon;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {
    List<ChiTietHoaDon> findByHoaDon_MaHD(String maHoaDon);

    List<ChiTietHoaDon> findByChiTietSanPham_Id(String id);

    List<ChiTietHoaDon> findBySoLuongXuatGreaterThan(Integer soLuongXuat);
}
