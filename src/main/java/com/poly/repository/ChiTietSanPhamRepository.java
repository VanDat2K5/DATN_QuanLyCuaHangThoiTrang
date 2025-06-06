package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.entity.ChiTietSanPham;

public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, String> {
    List<ChiTietSanPham> findAll();

    List<ChiTietSanPham> findBySanPham_MaSP(String maSP);

    List<ChiTietSanPham> findByMau_MaMau(String maMau);

    List<ChiTietSanPham> findBySize_MaSize(String maSize);

    List<ChiTietSanPham> findBySoLuongLessThanEqual(Integer soLuong);
}
