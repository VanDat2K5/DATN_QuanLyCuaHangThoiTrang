package com.poly.repository;

import com.poly.entity.HoaDon;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate;

public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    List<HoaDon> findByKhachHang_MaKH(String maKH);

    List<HoaDon> findByNhanVien_MaNV(String maNV);

    List<HoaDon> findByTrangThai(String trangThai);

    List<HoaDon> findByNgayLapBetween(LocalDate startDate, LocalDate endDate);

    List<HoaDon> findByPtThanhToan(String ptThanhToan);

    List<HoaDon> findByTrangThaiAndPtThanhToan(String trangThai, String ptThanhToan);

    @Modifying
    @Transactional
    @Query("UPDATE HoaDon h SET h.trangThai = :trangThai WHERE h.maHD = :maHD")
    int updateTrangThaiByMaHD(String maHD, String trangThai);
    
    Page<HoaDon> findByMaHDContainingIgnoreCase(String keyword, Pageable pageable);

}