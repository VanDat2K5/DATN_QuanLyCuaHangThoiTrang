package com.poly.service;

import com.poly.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface NhanVienService {
    NhanVien save(NhanVien nhanVien);

    Optional<NhanVien> findById(String id);

    List<NhanVien> findAll();

    void deleteById(String id);

    Page<NhanVien> findAll(Pageable pageable);

    List<NhanVien> findByTenNVContaining(String tenNV);

    List<NhanVien> findBySoDT(String soDT);

    List<NhanVien> findByEmail(String email);

    boolean existsById(String id);

    List<NhanVien> findByIsAdmin(Boolean isAdmin);

    List<NhanVien> findByIsActivity(Boolean isActivity);

    List<NhanVien> findByIsAdminAndIsActivity(Boolean isAdmin, Boolean isActivity);
}