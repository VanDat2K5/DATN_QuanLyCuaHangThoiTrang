package com.poly.repository;

import com.poly.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findBySanPham_MaSP(String maSP);

    List<HinhAnh> findByHinhAnhContaining(String hinhAnh);
}