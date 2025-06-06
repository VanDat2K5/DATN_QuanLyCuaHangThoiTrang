package com.poly.repository;

import com.poly.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SizeRepository extends JpaRepository<Size, String> {
    List<Size> findByTenSizeContaining(String tenSize);
}