package com.poly.service.impl;

import com.poly.entity.HinhAnh;
import com.poly.entity.SanPham;
import com.poly.repository.HinhAnhRepository;
import com.poly.service.HinhAnhService;
import com.poly.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class HinhAnhServiceImpl implements HinhAnhService {

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Autowired
    private SanPhamService sanPhamService;

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

    @Override
    public void storeImages(SanPham sp, MultipartFile[] images) throws IOException {
        for (MultipartFile file : images) {
            String fileName = file.getOriginalFilename(); // ✅ lấy tên file gốc

            if (fileName == null || fileName.isBlank()) continue;

            HinhAnh img = new HinhAnh();
            img.setSanPham(sp);
            img.setHinhAnh(fileName); // ✅ chỉ lưu tên file vào DB
            hinhAnhRepository.save(img); // ✅ đúng biến repo
        }
    }

}
