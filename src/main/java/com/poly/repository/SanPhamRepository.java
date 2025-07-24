package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SanPhamRepository extends JpaRepository<SanPham, String> {
    List<SanPham> findAll();

    List<SanPham> findByTenSPContaining(String tenSP);

    List<SanPham> findByLoaiSP_MaLoaiSP(String maLoaiSP);

    // Thêm các method tìm kiếm theo giới tính
    List<SanPham> findByGioiTinh(SanPham.Gender gioiTinh);

    List<SanPham> findByLoaiSP_MaLoaiSPAndGioiTinh(String maLoaiSP, SanPham.Gender gioiTinh);

    List<SanPham> findByTenSPContainingAndGioiTinh(String tenSP, SanPham.Gender gioiTinh);

    Page<SanPham> findByMaSPContainingIgnoreCase(String maSP, Pageable pageable);
}
