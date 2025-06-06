package com.poly.service;

import com.poly.entity.DiaChiNhanHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface DiaChiNhanHangService {
    DiaChiNhanHang save(DiaChiNhanHang diaChiNhanHang);

    Optional<DiaChiNhanHang> findById(Integer id);

    List<DiaChiNhanHang> findAll();

    void deleteById(Integer id);

    Page<DiaChiNhanHang> findAll(Pageable pageable);

    List<DiaChiNhanHang> findByKhachHang_MaKH(String maKhachHang);

    List<DiaChiNhanHang> findByDcNhanHangContaining(String diaChi);

    boolean existsById(Integer id);
}