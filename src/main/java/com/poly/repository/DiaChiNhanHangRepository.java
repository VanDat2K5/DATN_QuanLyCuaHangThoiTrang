package com.poly.repository;

import com.poly.entity.DiaChiNhanHang;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaChiNhanHangRepository extends JpaRepository<DiaChiNhanHang, Integer> {
    List<DiaChiNhanHang> findByKhachHang_MaKH(String maKH);

    List<DiaChiNhanHang> findByDcNhanHangContaining(String dcNhanHang);
}