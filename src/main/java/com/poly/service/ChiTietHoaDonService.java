package com.poly.service;

import com.poly.entity.ChiTietHoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ChiTietHoaDonService {
    ChiTietHoaDon save(ChiTietHoaDon chiTietHoaDon);

    // List<ChiTietHoaDon> findByMaHD(String maHoaDon);

    Optional<ChiTietHoaDon> findById(Integer id);

    List<ChiTietHoaDon> findAll();

    void deleteById(Integer id);

    Page<ChiTietHoaDon> findAll(Pageable pageable);

    List<ChiTietHoaDon> findByHoaDon_MaHD(String maHoaDon);

    List<ChiTietHoaDon> findByChiTietSanPham_Id(String id);

    List<ChiTietHoaDon> findBySoLuongXuatGreaterThan(Integer soLuongXuat);

    boolean existsById(Integer id);
}