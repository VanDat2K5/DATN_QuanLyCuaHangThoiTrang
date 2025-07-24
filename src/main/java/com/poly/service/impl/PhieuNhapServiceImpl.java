package com.poly.service.impl;

import com.poly.entity.PhieuNhap;
import com.poly.repository.PhieuNhapRepository;
import com.poly.service.PhieuNhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
@Transactional
public class PhieuNhapServiceImpl implements PhieuNhapService {

    @Autowired
    private PhieuNhapRepository phieuNhapRepository;

    @Override
    public PhieuNhap save(PhieuNhap phieuNhap) {
        return phieuNhapRepository.save(phieuNhap);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhieuNhap> findById(String id) {
        return phieuNhapRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhieuNhap> findAll() {
        return phieuNhapRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        phieuNhapRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhieuNhap> findAll(Pageable pageable) {
        return phieuNhapRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhieuNhap> findByNhanVien_MaNV(String maNhanVien) {
        return phieuNhapRepository.findByNhanVien_MaNV(maNhanVien);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhieuNhap> findByNgayNhapBetween(LocalDate startDate, LocalDate endDate) {
        return phieuNhapRepository.findByNgayNhapBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return phieuNhapRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhieuNhap> findByMaPNContainingIgnoreCase(String maPN, Pageable pageable) {
        return phieuNhapRepository.findByMaPNContainingIgnoreCase(maPN, pageable);
    }
}