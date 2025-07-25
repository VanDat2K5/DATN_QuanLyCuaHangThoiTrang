package com.poly.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueReportDTO {
    private LocalDate date;
    private BigDecimal totalRevenue; // Tổng doanh thu
    private BigDecimal totalCost; // Tổng chi phí nhập hàng
    private BigDecimal totalProfit; // Lợi nhuận = doanh thu - chi phí
    private Integer totalOrders; // Số đơn hàng
    private Integer totalProductsSold; // Số sản phẩm bán ra
    private Integer totalProductsImported; // Số sản phẩm nhập vào
    private List<CostByMonthDTO> costByMonth;
}