package com.poly.service;

import com.poly.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ChiTietSanPhamService {
    ChiTietSanPham save(ChiTietSanPham chiTietSanPham);

    Optional<ChiTietSanPham> findById(String id);

    List<ChiTietSanPham> findAll();

    void deleteById(String id);

    Page<ChiTietSanPham> findAll(Pageable pageable);

    List<ChiTietSanPham> findBySanPham_MaSP(String maSP);

    List<ChiTietSanPham> findByMau_MaMau(String maMau);

    List<ChiTietSanPham> findBySize_MaSize(String maSize);

    List<ChiTietSanPham> findBySoLuongLessThanEqual(Integer soLuong);

    boolean existsById(String id);

    Page<ChiTietSanPham> findBySanPham_MaSPContainingIgnoreCase(String maSP, Pageable pageable);

    Optional<ChiTietSanPham> findBySanPham_TenSPAndMau_MaMauAndSize_MaSize(String tenSP, String maMau,
            String maSize);
}