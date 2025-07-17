package com.poly.service;

import com.poly.dto.RevenueReportDTO;
import java.time.LocalDate;

public interface RevenueReportService {

    // Tổng hợp doanh thu theo khoảng thời gian
    RevenueReportDTO getTotalRevenueSummary(LocalDate startDate, LocalDate endDate);
}