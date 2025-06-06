package com.poly.service.impl;

import com.poly.entity.KhachHang;
import com.poly.repository.KhachHangRepository;
import com.poly.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Override
    public KhachHang save(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KhachHang> findById(String id) {
        return khachHangRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhachHang> findAll() {
        return khachHangRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        khachHangRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KhachHang> findAll(Pageable pageable) {
        return khachHangRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhachHang> findByTenKHContaining(String ten) {
        return khachHangRepository.findByTenKHContaining(ten);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhachHang> findBySoDT(String soDT) {
        return khachHangRepository.findBySoDT(soDT);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return khachHangRepository.existsById(id);
    }
}