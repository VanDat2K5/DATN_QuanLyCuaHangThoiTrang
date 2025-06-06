package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.entity.KhachHang;
import java.util.List;

public interface KhachHangRepository extends JpaRepository<KhachHang, String> {
    List<KhachHang> findByTenKHContaining(String tenKH);

    List<KhachHang> findBySoDT(String soDT);
}
