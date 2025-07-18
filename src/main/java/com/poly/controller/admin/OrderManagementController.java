package com.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderManagementController {
    @GetMapping("/admin/order-management")
    public String orderManagement() {
        return "admin/order-management";
    }
}
