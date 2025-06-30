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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<HinhAnh> storeImages(String maSP, MultipartFile[] files) throws IOException {
        // 1) Thư mục gốc uploads/{maSP}
        Path uploadDir = Paths.get("uploads", maSP);
        if (Files.notExists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 2) Lấy entity SanPham
        SanPham sp = sanPhamService.findById(maSP)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy SP " + maSP));

        List<HinhAnh> result = new ArrayList<>();
        for (MultipartFile f : files) {
            if (f.isEmpty()) continue;

            // 3) Đặt tên file cho unique
            String filename = System.currentTimeMillis() + "_" + f.getOriginalFilename();
            Path target = uploadDir.resolve(filename);

            // 4) Ghi file ra disk
            f.transferTo(target.toFile());

            // 5) Chuẩn bị entity HinhAnh
            HinhAnh ha = new HinhAnh();
            ha.setSanPham(sp);

            // Lưu đường dẫn public (sẽ map qua /uploads/** bằng WebMvcConfig)
            ha.setHinhAnh("/uploads/" + maSP + "/" + filename);

            result.add(hinhAnhRepository.save(ha));
        }
        return result;
    }

}