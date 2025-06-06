package com.poly.service;

import com.poly.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface SizeService {
    Size save(Size size);

    Optional<Size> findById(String id);

    List<Size> findAll();

    void deleteById(String id);

    Page<Size> findAll(Pageable pageable);

    List<Size> findByTenSizeContaining(String tenSize);

    boolean existsById(String id);
}