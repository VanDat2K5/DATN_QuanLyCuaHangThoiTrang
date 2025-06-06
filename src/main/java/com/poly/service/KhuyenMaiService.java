package com.poly.service;

import com.poly.entity.KhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public interface KhuyenMaiService {
    KhuyenMai save(KhuyenMai khuyenMai);

    Optional<KhuyenMai> findById(String id);

    List<KhuyenMai> findAll();

    void deleteById(String id);

    Page<KhuyenMai> findAll(Pageable pageable);

    List<KhuyenMai> findByTenKMContaining(String tenKM);

    List<KhuyenMai> findByNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(Date startDate, Date endDate);

    List<KhuyenMai> findByGiamGreaterThanEqual(Integer giam);

    boolean existsById(String id);
}