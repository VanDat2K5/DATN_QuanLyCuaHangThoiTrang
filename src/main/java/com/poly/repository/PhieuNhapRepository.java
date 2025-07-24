package com.poly.repository;

import com.poly.entity.PhieuNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, String> {
    List<PhieuNhap> findByNhanVien_MaNV(String maNV);

    List<PhieuNhap> findByNgayNhapBetween(LocalDate startDate, LocalDate endDate);

    Page<PhieuNhap> findByMaPNContainingIgnoreCase(String maPN, Pageable pageable);
}