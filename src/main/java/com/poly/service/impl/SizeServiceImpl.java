package com.poly.service.impl;

import com.poly.entity.Size;
import com.poly.repository.SizeRepository;
import com.poly.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Size> findById(String id) {
        return sizeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        sizeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Size> findAll(Pageable pageable) {
        return sizeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Size> findByTenSizeContaining(String tenSize) {
        return sizeRepository.findByTenSizeContaining(tenSize);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return sizeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Size> findByMaSizeContainingIgnoreCase(String maSize, Pageable pageable) {
        return sizeRepository.findByMaSizeContainingIgnoreCase(maSize, pageable);
    }
}