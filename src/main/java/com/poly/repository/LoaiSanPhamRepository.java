package com.poly.repository;

import com.poly.entity.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, String> {
    List<LoaiSanPham> findByLoaiSPContaining(String loaiSP);
}