package com.poly.repository;

import com.poly.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    List<NhanVien> findByTenNVContaining(String tenNV);

    List<NhanVien> findBySoDT(String soDT);

    List<NhanVien> findByEmail(String email);

    List<NhanVien> findByIsAdmin(Boolean isAdmin);

    List<NhanVien> findByIsActivity(Boolean isActivity);

    List<NhanVien> findByIsAdminAndIsActivity(Boolean isAdmin, Boolean isActivity);
}