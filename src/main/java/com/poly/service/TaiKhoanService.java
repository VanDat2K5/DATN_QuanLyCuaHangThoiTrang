package com.poly.service;

import com.poly.entity.KhachHang;
import com.poly.entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface TaiKhoanService {
    TaiKhoan save(TaiKhoan taiKhoan);

    Optional<TaiKhoan> findById(Integer id);

    List<TaiKhoan> findAll();

    void deleteById(Integer id);

    Page<TaiKhoan> findAll(Pageable pageable);

    Optional<TaiKhoan> findByTenTK(String tenTK);

    List<TaiKhoan> findByTenTKContaining(String tenTK);

    Optional<TaiKhoan> findByTenTKAndMatKhau(String tenTK, String matKhau);

    Optional<TaiKhoan> findByKhachHangAndMatKhau(KhachHang khachHang, String matKhau);

    // Tìm tài khoản theo khách hàng
    Optional<TaiKhoan> findByKhachHang(KhachHang khachHang);

    boolean existsById(Integer id);
}