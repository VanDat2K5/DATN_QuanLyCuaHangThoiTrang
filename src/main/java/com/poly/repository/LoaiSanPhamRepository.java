package com.poly.repository;

import com.poly.entity.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, String> {
    List<LoaiSanPham> findByLoaiSPContaining(String loaiSP);

    Page<LoaiSanPham> findByMaLoaiSPContainingIgnoreCase(String maLoaiSP, Pageable pageable);
}