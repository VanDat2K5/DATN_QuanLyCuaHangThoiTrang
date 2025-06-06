package com.poly.service.impl;

import com.poly.entity.Mau;
import com.poly.repository.MauRepository;
import com.poly.service.MauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MauServiceImpl implements MauService {

    @Autowired
    private MauRepository mauRepository;

    @Override
    public Mau save(Mau mau) {
        return mauRepository.save(mau);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mau> findById(String id) {
        return mauRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mau> findAll() {
        return mauRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        mauRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Mau> findAll(Pageable pageable) {
        return mauRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mau> findByTenMauContaining(String tenMau) {
        return mauRepository.findByTenMauContaining(tenMau);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return mauRepository.existsById(id);
    }
}