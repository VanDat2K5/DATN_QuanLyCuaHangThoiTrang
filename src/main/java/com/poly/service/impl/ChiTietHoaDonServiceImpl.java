package com.poly.service.impl;

import com.poly.entity.ChiTietHoaDon;
import com.poly.repository.ChiTietHoaDonRepository;
import com.poly.service.ChiTietHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChiTietHoaDonServiceImpl implements ChiTietHoaDonService {

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Override
    public ChiTietHoaDon save(ChiTietHoaDon chiTietHoaDon) {
        return chiTietHoaDonRepository.save(chiTietHoaDon);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChiTietHoaDon> findById(Integer id) {
        return chiTietHoaDonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietHoaDon> findAll() {
        return chiTietHoaDonRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        chiTietHoaDonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChiTietHoaDon> findAll(Pageable pageable) {
        return chiTietHoaDonRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietHoaDon> findByHoaDon_MaHD(String maHoaDon) {
        return chiTietHoaDonRepository.findByHoaDon_MaHD(maHoaDon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietHoaDon> findByChiTietSanPham_Id(String id) {
        return chiTietHoaDonRepository.findByChiTietSanPham_Id(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietHoaDon> findBySoLuongXuatGreaterThan(Integer soLuongXuat) {
        return chiTietHoaDonRepository.findBySoLuongXuatGreaterThan(soLuongXuat);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return chiTietHoaDonRepository.existsById(id);
    }
}