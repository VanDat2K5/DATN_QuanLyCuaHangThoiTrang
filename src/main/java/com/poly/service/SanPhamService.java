package com.poly.service;

import com.poly.dto.SanPhamViewDTO;
import com.poly.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface SanPhamService {
    SanPham save(SanPham sanPham);

    Optional<SanPham> findById(String id);

    List<SanPham> findAll();

    void deleteById(String id);

    Page<SanPham> findAll(Pageable pageable);

    List<SanPham> findByTenSPContaining(String tenSP);

    List<SanPham> findByLoaiSanPham_MaLoaiSP(String maLoaiSP);

    boolean existsById(String id);


    Page<SanPhamViewDTO> findAllSanPhamDTO(Pageable pageable);

    // Thêm các method tìm kiếm theo giới tính
    List<SanPham> findByGioiTinh(SanPham.Gender gioiTinh);

    List<SanPham> findByLoaiSanPham_MaLoaiSPAndGioiTinh(String maLoaiSP, SanPham.Gender gioiTinh);

    List<SanPham> findByTenSPContainingAndGioiTinh(String tenSP, SanPham.Gender gioiTinh);
}