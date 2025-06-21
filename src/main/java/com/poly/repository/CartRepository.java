package com.poly.repository;

import com.poly.model.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository {
    List<CartItem> findByUserId(String userId);

    void deleteByUserIdAndProductId(String userId, String productId);
}