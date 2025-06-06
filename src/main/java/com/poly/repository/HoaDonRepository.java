package com.poly.repository;

import com.poly.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;

public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    List<HoaDon> findByKhachHang_MaKH(String maKH);

    List<HoaDon> findByNhanVien_MaNV(String maNV);

    List<HoaDon> findByTrangThai(String trangThai);

    List<HoaDon> findByNgayLapBetween(LocalDate startDate, LocalDate endDate);
}