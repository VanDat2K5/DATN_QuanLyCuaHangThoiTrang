package com.poly.controller.customer;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service quản lý giới hạn gửi yêu cầu đặt lại mật khẩu
 * Chống spam: tối đa 3 lần gửi yêu cầu trong 15 phút cho mỗi email
 */
@Service
public class ResetPasswordLimiter {
    
    // Giới hạn số lần gửi yêu cầu đặt lại mật khẩu
    private static final int LIMIT = 3;
    
    // Thời gian cửa sổ giới hạn (15 phút tính bằng milliseconds)
    // 15 phút × 60 giây × 1000 milliseconds = 900,000ms
    private static final long WINDOW = 15 * 60 * 1000;
    
    // Map lưu trữ thông tin đếm cho từng email
    // Sử dụng ConcurrentHashMap để thread-safe khi nhiều user cùng gửi yêu cầu
    private final ConcurrentHashMap<String, ResetPasswordLog> logs = new ConcurrentHashMap<>();

    /**
     * Kiểm tra xem email có thể gửi yêu cầu đặt lại mật khẩu hay không
     * @param email Email cần kiểm tra
     * @return true nếu có thể gửi, false nếu đã vượt giới hạn
     */
    public boolean canSend(String email) {
        // Lấy thời gian hiện tại (milliseconds)
        long now = System.currentTimeMillis();
        
        // Lấy thông tin đếm của email này từ Map
        ResetPasswordLog log = logs.get(email);
        
        // Trường hợp 1: Email chưa từng gửi yêu cầu HOẶC đã hết thời gian 15 phút
        if (log == null || now - log.lastSentTime > WINDOW) {
            // Tạo mới hoặc reset: đếm = 1, thời gian = hiện tại
            logs.put(email, new ResetPasswordLog(1, now));
            return true; // Cho phép gửi
        } 
        // Trường hợp 2: Chưa đạt giới hạn 3 lần trong 15 phút
        else if (log.count < LIMIT) {
            // Tăng số đếm và cập nhật thời gian gửi cuối
            log.count++;
            log.lastSentTime = now;
            return true; // Cho phép gửi
        } 
        // Trường hợp 3: Đã đạt giới hạn 3 lần trong 15 phút
        else {
            return false; // Từ chối gửi
        }
    }

    /**
     * Lớp nội bộ lưu trữ thông tin đếm cho mỗi email
     */
    private static class ResetPasswordLog {
        // Số lần đã gửi yêu cầu đặt lại mật khẩu
        int count;
        
        // Thời gian gửi yêu cầu cuối cùng (milliseconds)
        long lastSentTime;
        
        /**
         * Constructor tạo log mới
         * @param count Số lần đã gửi
         * @param lastSentTime Thời gian gửi cuối
         */
        ResetPasswordLog(int count, long lastSentTime) {
            this.count = count;
            this.lastSentTime = lastSentTime;
        }
    }
} 