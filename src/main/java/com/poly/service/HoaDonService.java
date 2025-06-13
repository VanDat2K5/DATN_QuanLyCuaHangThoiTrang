package com.poly.service;

import com.poly.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface HoaDonService {
    HoaDon save(HoaDon hoaDon);

    Optional<HoaDon> findById(String id);

    List<HoaDon> findAll();

    void deleteById(String id);

    Page<HoaDon> findAll(Pageable pageable);

    List<HoaDon> findByKhachHang_MaKH(String maKhachHang);

    List<HoaDon> findByNhanVien_MaNV(String maNhanVien);

    List<HoaDon> findByTrangThai(String trangThai);

    List<HoaDon> findByNgayLapBetween(LocalDate startDate, LocalDate endDate);

    List<HoaDon> findByPtThanhToan(String ptThanhToan);

    List<HoaDon> findByTrangThaiAndPtThanhToan(String trangThai, String ptThanhToan);

    boolean existsById(String id);
}