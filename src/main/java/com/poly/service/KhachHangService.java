package com.poly.service;

import com.poly.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    // CRUD operations
    KhachHang save(KhachHang khachHang);

    Optional<KhachHang> findById(String id);

    List<KhachHang> findAll();

    void deleteById(String id);

    // Additional useful methods
    Page<KhachHang> findAll(Pageable pageable);

    List<KhachHang> findByTenKHContaining(String ten);

    List<KhachHang> findBySoDT(String soDT);

    Optional<KhachHang> findByEmail(String email);

    boolean existsById(String id);

    Page<KhachHang> findByMaKHContainingIgnoreCase(String maKH, Pageable pageable);
}