package com.poly.controller.admin;

import com.poly.dto.RevenueReportDTO;
import com.poly.dto.CostByMonthDTO;
import com.poly.service.RevenueReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/revenue")
public class RevenueReportController {

    @Autowired
    private RevenueReportService revenueReportService;

    @GetMapping
    public String showRevenueReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        // Mặc định là tháng hiện tại
        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            startDate = now.withDayOfMonth(1);
            endDate = now.withDayOfMonth(now.lengthOfMonth());
        }

        // Lấy tổng hợp doanh thu
        RevenueReportDTO summary = revenueReportService.getTotalRevenueSummary(startDate, endDate);
        model.addAttribute("summary", summary);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/revenue-report";
    }
}