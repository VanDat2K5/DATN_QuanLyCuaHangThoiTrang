package com.poly.service.impl;

import com.poly.entity.HinhAnh;
import com.poly.repository.HinhAnhRepository;
import com.poly.service.HinhAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HinhAnhServiceImpl implements HinhAnhService {

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Override
    public HinhAnh save(HinhAnh hinhAnh) {
        return hinhAnhRepository.save(hinhAnh);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HinhAnh> findById(Integer id) {
        return hinhAnhRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HinhAnh> findAll() {
        return hinhAnhRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        hinhAnhRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HinhAnh> findAll(Pageable pageable) {
        return hinhAnhRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HinhAnh> findBySanPham_MaSP(String maSP) {
        return hinhAnhRepository.findBySanPham_MaSP(maSP);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HinhAnh> findByHinhAnhContaining(String hinhAnh) {
        return hinhAnhRepository.findByHinhAnhContaining(hinhAnh);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return hinhAnhRepository.existsById(id);
    }
}