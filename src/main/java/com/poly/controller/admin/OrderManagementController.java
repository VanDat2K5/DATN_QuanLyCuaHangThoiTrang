package com.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class OrderManagementController {
    @GetMapping("/order-management")
    public String orderManagement() {
        return "admin/order-management";
    }
}
