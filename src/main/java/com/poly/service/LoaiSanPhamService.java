package com.poly.service;

import com.poly.entity.LoaiSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface LoaiSanPhamService {
    LoaiSanPham save(LoaiSanPham loaiSanPham);

    Optional<LoaiSanPham> findById(String id);

    List<LoaiSanPham> findAll();

    void deleteById(String id);

    Page<LoaiSanPham> findAll(Pageable pageable);

    List<LoaiSanPham> findByLoaiSPContaining(String loaiSP);

    boolean existsById(String id);

    Page<LoaiSanPham> findByMaLoaiSPContainingIgnoreCase(String maLoaiSP, Pageable pageable);
}