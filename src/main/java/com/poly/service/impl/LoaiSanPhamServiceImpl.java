package com.poly.service.impl;

import com.poly.entity.LoaiSanPham;
import com.poly.repository.LoaiSanPhamRepository;
import com.poly.service.LoaiSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoaiSanPhamServiceImpl implements LoaiSanPhamService {

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Override
    public LoaiSanPham save(LoaiSanPham loaiSanPham) {
        return loaiSanPhamRepository.save(loaiSanPham);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoaiSanPham> findById(String id) {
        return loaiSanPhamRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoaiSanPham> findAll() {
        return loaiSanPhamRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        loaiSanPhamRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoaiSanPham> findAll(Pageable pageable) {
        return loaiSanPhamRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoaiSanPham> findByLoaiSPContaining(String loaiSP) {
        return loaiSanPhamRepository.findByLoaiSPContaining(loaiSP);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return loaiSanPhamRepository.existsById(id);
    }
}