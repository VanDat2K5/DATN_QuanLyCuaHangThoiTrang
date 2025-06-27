package com.poly.service.impl;

import com.poly.entity.KhachHang;
import com.poly.entity.TaiKhoan;
import com.poly.repository.TaiKhoanRepository;
import com.poly.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaiKhoanServiceImpl implements TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public TaiKhoan save(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaiKhoan> findById(Integer id) {
        return taiKhoanRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaiKhoan> findAll() {
        return taiKhoanRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        taiKhoanRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaiKhoan> findAll(Pageable pageable) {
        return taiKhoanRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaiKhoan> findByTenTK(String tenTK) {
        return taiKhoanRepository.findByTenTK(tenTK);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaiKhoan> findByTenTKContaining(String tenTK) {
        return taiKhoanRepository.findByTenTKContaining(tenTK);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaiKhoan> findByTenTKAndMatKhau(String tenTK, String matKhau) {
        return taiKhoanRepository.findByTenTKAndMatKhau(tenTK, matKhau);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaiKhoan> findByKhachHangAndMatKhau(KhachHang khachHang, String matKhau) {
        return taiKhoanRepository.findByKhachHangAndMatKhau(khachHang, matKhau);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaiKhoan> findByKhachHang(KhachHang khachHang) {
        return taiKhoanRepository.findByKhachHang(khachHang);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return taiKhoanRepository.existsById(id);
    }
}