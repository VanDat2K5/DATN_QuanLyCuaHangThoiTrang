package com.poly.repository;

import com.poly.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, String> {
    List<KhuyenMai> findByTenKMContaining(String tenKM);

    List<KhuyenMai> findByNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(Date currentDate, Date currentDate2);

    List<KhuyenMai> findByGiamGreaterThanEqual(Integer giam);

    Page<KhuyenMai> findByMaKMContainingIgnoreCase(String maKM, Pageable pageable);
}