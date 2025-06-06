package com.poly.service;

import com.poly.entity.ChiTietPhieuNhap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ChiTietPhieuNhapService {
    ChiTietPhieuNhap save(ChiTietPhieuNhap chiTietPhieuNhap);

    Optional<ChiTietPhieuNhap> findById(Integer id);

    List<ChiTietPhieuNhap> findAll();

    void deleteById(Integer id);

    Page<ChiTietPhieuNhap> findAll(Pageable pageable);

    List<ChiTietPhieuNhap> findByPhieuNhap_MaPN(String maPN);

    List<ChiTietPhieuNhap> findByChiTietSanPham_Id(String id);

    List<ChiTietPhieuNhap> findBySoLuongNhapGreaterThan(Integer soLuongNhap);

    boolean existsById(Integer id);
}