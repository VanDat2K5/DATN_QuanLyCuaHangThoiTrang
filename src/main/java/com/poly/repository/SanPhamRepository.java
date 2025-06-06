package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.entity.SanPham;

public interface SanPhamRepository extends JpaRepository<SanPham, String> {
    List<SanPham> findAll();

    List<SanPham> findByTenSPContaining(String tenSP);

    List<SanPham> findByLoaiSanPham_MaLoaiSP(String maLoaiSP);
}
