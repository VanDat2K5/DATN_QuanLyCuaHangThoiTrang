package com.poly.service.impl;

import com.poly.entity.ChiTietSanPham;
import com.poly.repository.ChiTietSanPhamRepository;
import com.poly.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChiTietSanPhamServiceImpl implements ChiTietSanPhamService {

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Override
    public ChiTietSanPham save(ChiTietSanPham chiTietSanPham) {
        if (chiTietSanPham.getId() == null || chiTietSanPham.getId().isEmpty()) {
            String id = chiTietSanPham.getSanPham().getMaSP()
                    + "_" + chiTietSanPham.getMau().getMaMau()
                    + "_" + chiTietSanPham.getSize().getMaSize();
            chiTietSanPham.setId(id);
            if (chiTietSanPhamRepository.existsById(id)) {
                throw new IllegalArgumentException("Chi tiết sản phẩm với ID này đã tồn tại!");
            }
            chiTietSanPham.setId(id);
        }
        return chiTietSanPhamRepository.save(chiTietSanPham);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChiTietSanPham> findById(String id) {
        return chiTietSanPhamRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietSanPham> findAll() {
        return chiTietSanPhamRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        chiTietSanPhamRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChiTietSanPham> findAll(Pageable pageable) {
        return chiTietSanPhamRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietSanPham> findBySanPham_MaSP(String maSP) {
        return chiTietSanPhamRepository.findBySanPham_MaSP(maSP);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietSanPham> findByMau_MaMau(String maMau) {
        return chiTietSanPhamRepository.findByMau_MaMau(maMau);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietSanPham> findBySize_MaSize(String maSize) {
        return chiTietSanPhamRepository.findBySize_MaSize(maSize);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChiTietSanPham> findBySoLuongLessThanEqual(Integer soLuong) {
        return chiTietSanPhamRepository.findBySoLuongLessThanEqual(soLuong);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return chiTietSanPhamRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChiTietSanPham> findBySanPham_MaSPContainingIgnoreCase(String maSP, Pageable pageable) {
        return chiTietSanPhamRepository.findBySanPham_MaSPContainingIgnoreCase(maSP, pageable);
    }
}