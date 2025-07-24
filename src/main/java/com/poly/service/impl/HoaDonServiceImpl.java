package com.poly.service.impl;

import com.poly.entity.HoaDon;
import com.poly.repository.HoaDonRepository;
import com.poly.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Override
    public HoaDon save(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HoaDon> findById(String id) {
        return hoaDonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        hoaDonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoaDon> findAll(Pageable pageable) {
        return hoaDonRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findByKhachHang_MaKH(String maKhachHang) {
        return hoaDonRepository.findByKhachHang_MaKH(maKhachHang);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findByNhanVien_MaNV(String maNhanVien) {
        return hoaDonRepository.findByNhanVien_MaNV(maNhanVien);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findByTrangThai(String trangThai) {
        return hoaDonRepository.findByTrangThai(trangThai);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findByNgayLapBetween(LocalDate startDate, LocalDate endDate) {
        return hoaDonRepository.findByNgayLapBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findByPtThanhToan(String ptThanhToan) {
        return hoaDonRepository.findByPtThanhToan(ptThanhToan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findByTrangThaiAndPtThanhToan(String trangThai, String ptThanhToan) {
        return hoaDonRepository.findByTrangThaiAndPtThanhToan(trangThai, ptThanhToan);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return hoaDonRepository.existsById(id);
    }

    @Override
    public int updateTrangThaiByMaHD(String maHD, String trangThai) {
        return hoaDonRepository.updateTrangThaiByMaHD(maHD, trangThai);
    }

    @Override
    public Page<HoaDon> findByMaHDContainingIgnoreCase(String keyword, Pageable pageable) {
        return hoaDonRepository.findByMaHDContainingIgnoreCase(keyword, pageable);
    }
}