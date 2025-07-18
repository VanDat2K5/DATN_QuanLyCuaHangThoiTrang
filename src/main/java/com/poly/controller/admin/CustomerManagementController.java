package com.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerManagementController {
    @GetMapping("/admin/customer-management")
    public String customerManagement() {
        return "admin/customer-management";
    }
}