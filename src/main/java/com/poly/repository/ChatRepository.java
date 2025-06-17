package com.poly.repository;

import com.poly.model.ChatItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository {
    List<ChatItem> findByMaKH(String maKH);
}