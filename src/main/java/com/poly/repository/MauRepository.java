package com.poly.repository;

import com.poly.entity.Mau;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MauRepository extends JpaRepository<Mau, String> {
    List<Mau> findByTenMauContaining(String tenMau);

    Page<Mau> findByMaMauContainingIgnoreCase(String maMau, Pageable pageable);
}