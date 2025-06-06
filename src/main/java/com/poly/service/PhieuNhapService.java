package com.poly.service;

import com.poly.entity.PhieuNhap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface PhieuNhapService {
    PhieuNhap save(PhieuNhap phieuNhap);

    Optional<PhieuNhap> findById(String id);

    List<PhieuNhap> findAll();

    void deleteById(String id);

    Page<PhieuNhap> findAll(Pageable pageable);

    List<PhieuNhap> findByNhanVien_MaNV(String maNhanVien);

    List<PhieuNhap> findByNgayNhapBetween(LocalDate startDate, LocalDate endDate);

    boolean existsById(String id);
}