package com.poly.service.impl;

import com.poly.entity.KhuyenMai;
import com.poly.repository.KhuyenMaiRepository;
import com.poly.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
@Transactional
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @Override
    public KhuyenMai save(KhuyenMai khuyenMai) {
        return khuyenMaiRepository.save(khuyenMai);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KhuyenMai> findById(String id) {
        return khuyenMaiRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhuyenMai> findAll() {
        return khuyenMaiRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        khuyenMaiRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KhuyenMai> findAll(Pageable pageable) {
        return khuyenMaiRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhuyenMai> findByTenKMContaining(String tenKM) {
        return khuyenMaiRepository.findByTenKMContaining(tenKM);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhuyenMai> findByNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(Date startDate, Date endDate) {
        return khuyenMaiRepository.findByNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhuyenMai> findByGiamGreaterThanEqual(Integer giam) {
        return khuyenMaiRepository.findByGiamGreaterThanEqual(giam);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return khuyenMaiRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KhuyenMai> findByMaKMContainingIgnoreCase(String maKM, Pageable pageable) {
        return khuyenMaiRepository.findByMaKMContainingIgnoreCase(maKM, pageable);
    }
}