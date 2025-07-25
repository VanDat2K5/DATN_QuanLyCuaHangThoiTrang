package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, String> {
    List<ChiTietSanPham> findAll();

    List<ChiTietSanPham> findBySanPham_MaSP(String maSP);

    List<ChiTietSanPham> findByMau_MaMau(String maMau);

    List<ChiTietSanPham> findBySize_MaSize(String maSize);

    List<ChiTietSanPham> findBySoLuongLessThanEqual(Integer soLuong);

    Page<ChiTietSanPham> findBySanPham_MaSPContainingIgnoreCase(String maSP, Pageable pageable);

    java.util.Optional<ChiTietSanPham> findBySanPham_TenSPAndMau_MaMauAndSize_MaSize(String tenSP, String maMau,
            String maSize);
}
