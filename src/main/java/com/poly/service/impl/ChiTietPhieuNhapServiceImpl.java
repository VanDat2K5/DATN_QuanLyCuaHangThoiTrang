package com.poly.service.impl;

import com.poly.entity.ChiTietPhieuNhap;
import com.poly.repository.ChiTietPhieuNhapRepository;
import com.poly.service.ChiTietPhieuNhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChiTietPhieuNhapServiceImpl implements ChiTietPhieuNhapService {

    @Autowired
    private ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;

    @Override
    public ChiTietPhieuNhap save(ChiTietPhieuNhap chiTietPhieuNhap) {
        return chiTietPhieuNhapRepository.save(chiTietPhieuNhap);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChiTietPhieuNhap> findById(Integer id) {
        return chiTietPhieuNhapRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietPhieuNhap> findAll() {
        return chiTietPhieuNhapRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        chiTietPhieuNhapRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChiTietPhieuNhap> findAll(Pageable pageable) {
        return chiTietPhieuNhapRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietPhieuNhap> findByPhieuNhap_MaPN(String maPN) {
        return chiTietPhieuNhapRepository.findByPhieuNhap_MaPN(maPN);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietPhieuNhap> findByChiTietSanPham_Id(String id) {
        return chiTietPhieuNhapRepository.findByChiTietSanPham_Id(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietPhieuNhap> findBySoLuongNhapGreaterThan(Integer soLuongNhap) {
        return chiTietPhieuNhapRepository.findBySoLuongNhapGreaterThan(soLuongNhap);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return chiTietPhieuNhapRepository.existsById(id);
    }
}