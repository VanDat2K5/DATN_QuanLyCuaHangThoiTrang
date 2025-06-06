package com.poly.service.impl;

import com.poly.entity.SanPham;
import com.poly.repository.SanPhamRepository;
import com.poly.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SanPhamServiceImpl implements SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Override
    public SanPham save(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SanPham> findById(String id) {
        return sanPhamRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SanPham> findAll() {
        return sanPhamRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SanPham> findAll(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SanPham> findByTenSPContaining(String tenSP) {
        return sanPhamRepository.findByTenSPContaining(tenSP);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SanPham> findByLoaiSanPham_MaLoaiSP(String maLoaiSP) {
        return sanPhamRepository.findByLoaiSanPham_MaLoaiSP(maLoaiSP);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return sanPhamRepository.existsById(id);
    }
}