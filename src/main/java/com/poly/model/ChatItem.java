package com.poly.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatItem {
    private String noiDung;
    private long thoiGian;
    private boolean vaiTro;
    private boolean trangThai;
}