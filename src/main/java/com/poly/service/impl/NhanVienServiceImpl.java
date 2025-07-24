package com.poly.service.impl;

import com.poly.entity.NhanVien;
import com.poly.repository.NhanVienRepository;
import com.poly.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Override
    public NhanVien save(NhanVien nhanVien) {
        return nhanVienRepository.save(nhanVien);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NhanVien> findById(String id) {
        return nhanVienRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NhanVien> findAll() {
        return nhanVienRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        nhanVienRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NhanVien> findAll(Pageable pageable) {
        return nhanVienRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NhanVien> findByTenNVContaining(String tenNV) {
        return nhanVienRepository.findByTenNVContaining(tenNV);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NhanVien> findBySoDT(String soDT) {
        return nhanVienRepository.findBySoDT(soDT);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NhanVien> findByEmail(String email) {
        return nhanVienRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return nhanVienRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NhanVien> findByIsAdmin(Boolean isAdmin) {
        return nhanVienRepository.findByIsAdmin(isAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NhanVien> findByIsActivity(Boolean isActivity) {
        return nhanVienRepository.findByIsActivity(isActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NhanVien> findByIsAdminAndIsActivity(Boolean isAdmin, Boolean isActivity) {
        return nhanVienRepository.findByIsAdminAndIsActivity(isAdmin, isActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NhanVien> findByMaNVContainingIgnoreCase(String maNV, Pageable pageable) {
        return nhanVienRepository.findByMaNVContainingIgnoreCase(maNV, pageable);
    }
}