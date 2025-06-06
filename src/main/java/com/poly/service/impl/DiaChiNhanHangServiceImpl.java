package com.poly.service.impl;

import com.poly.entity.DiaChiNhanHang;
import com.poly.repository.DiaChiNhanHangRepository;
import com.poly.service.DiaChiNhanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiaChiNhanHangServiceImpl implements DiaChiNhanHangService {

    @Autowired
    private DiaChiNhanHangRepository diaChiNhanHangRepository;

    @Override
    public DiaChiNhanHang save(DiaChiNhanHang diaChiNhanHang) {
        return diaChiNhanHangRepository.save(diaChiNhanHang);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DiaChiNhanHang> findById(Integer id) {
        return diaChiNhanHangRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiaChiNhanHang> findAll() {
        return diaChiNhanHangRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        diaChiNhanHangRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiaChiNhanHang> findAll(Pageable pageable) {
        return diaChiNhanHangRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiaChiNhanHang> findByKhachHang_MaKH(String maKhachHang) {
        return diaChiNhanHangRepository.findByKhachHang_MaKH(maKhachHang);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiaChiNhanHang> findByDcNhanHangContaining(String diaChi) {
        return diaChiNhanHangRepository.findByDcNhanHangContaining(diaChi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return diaChiNhanHangRepository.existsById(id);
    }
}