package com.poly.repository;

import com.poly.entity.KhachHang;
import com.poly.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    Optional<TaiKhoan> findByTenTK(String tenTK);

    List<TaiKhoan> findByTenTKContaining(String tenTK);

    Optional<TaiKhoan> findByTenTKAndMatKhau(String tenTK, String matKhau);

    Optional<TaiKhoan> findByKhachHangAndMatKhau(KhachHang khachHang, String matKhau);

    // Tìm tài khoản theo khách hàng
    Optional<TaiKhoan> findByKhachHang(KhachHang khachHang);
}