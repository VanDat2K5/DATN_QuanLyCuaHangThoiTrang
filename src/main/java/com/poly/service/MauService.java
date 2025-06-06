package com.poly.service;

import com.poly.entity.Mau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface MauService {
    Mau save(Mau mau);

    Optional<Mau> findById(String id);

    List<Mau> findAll();

    void deleteById(String id);

    Page<Mau> findAll(Pageable pageable);

    List<Mau> findByTenMauContaining(String tenMau);

    boolean existsById(String id);
}