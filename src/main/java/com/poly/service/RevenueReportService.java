package com.poly.service;

import com.poly.dto.RevenueReportDTO;
import java.time.LocalDate;
import java.util.List;
import com.poly.dto.CostByMonthDTO;

public interface RevenueReportService {

    // Tổng hợp doanh thu theo khoảng thời gian
    RevenueReportDTO getTotalRevenueSummary(LocalDate startDate, LocalDate endDate);

    // Tổng hợp chi phí theo từng tháng trong khoảng thời gian
    List<CostByMonthDTO> getCostByMonth(LocalDate startDate, LocalDate endDate);
}