package com.poly.repository;

import com.poly.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SizeRepository extends JpaRepository<Size, String> {
    List<Size> findByTenSizeContaining(String tenSize);

    Page<Size> findByMaSizeContainingIgnoreCase(String maSize, Pageable pageable);
}