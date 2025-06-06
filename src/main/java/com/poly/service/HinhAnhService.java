package com.poly.service;

import com.poly.entity.HinhAnh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface HinhAnhService {
    HinhAnh save(HinhAnh hinhAnh);

    Optional<HinhAnh> findById(Integer id);

    List<HinhAnh> findAll();

    void deleteById(Integer id);

    Page<HinhAnh> findAll(Pageable pageable);

    List<HinhAnh> findBySanPham_MaSP(String maSP);

    List<HinhAnh> findByHinhAnhContaining(String hinhAnh);

    boolean existsById(Integer id);
}