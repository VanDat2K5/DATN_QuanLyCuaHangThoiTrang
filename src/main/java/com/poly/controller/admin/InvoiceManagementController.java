package com.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InvoiceManagementController {
    @GetMapping("/admin/invoice-management")
    public String invoiceManagement() {
        return "admin/invoice-management";
    }
} 