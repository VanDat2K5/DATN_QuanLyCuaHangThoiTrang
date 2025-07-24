package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.entity.KhachHang;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhachHangRepository extends JpaRepository<KhachHang, String> {
    List<KhachHang> findByTenKHContaining(String tenKH);

    List<KhachHang> findBySoDT(String soDT);

    Optional<KhachHang> findByEmail(String email);

    Page<KhachHang> findByMaKHContainingIgnoreCase(String maKH, Pageable pageable);
}
