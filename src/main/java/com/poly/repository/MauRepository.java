package com.poly.repository;

import com.poly.entity.Mau;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MauRepository extends JpaRepository<Mau, String> {
    List<Mau> findByTenMauContaining(String tenMau);
}