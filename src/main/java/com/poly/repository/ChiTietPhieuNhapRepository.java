package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.ChiTietPhieuNhap;

@Repository
public interface ChiTietPhieuNhapRepository extends JpaRepository<ChiTietPhieuNhap, Integer> {
    List<ChiTietPhieuNhap> findByPhieuNhap_MaPN(String maPN);

    List<ChiTietPhieuNhap> findByChiTietSanPham_Id(String id);

    List<ChiTietPhieuNhap> findBySoLuongNhapGreaterThan(Integer soLuongNhap);
}