package com.poly.controller.admin;

import com.poly.entity.NhanVien;
import com.poly.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TestEmployeeController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/test-employee")
    public String testEmployee(Model model) {
        try {
            // Thử lấy dữ liệu từ database
            List<NhanVien> employees = nhanVienService.findAll();
            model.addAttribute("employees", employees);
            model.addAttribute("message", "Database connection successful. Found " + employees.size() + " employees.");
        } catch (Exception e) {
            // Nếu có lỗi database, tạo dữ liệu mẫu
            List<NhanVien> sampleEmployees = new ArrayList<>();
            
            NhanVien emp1 = new NhanVien();
            emp1.setMaNV("NV001");
            emp1.setTenNV("Nguyễn Văn An");
            emp1.setEmail("nvan@gmail.com");
            emp1.setSoDT("0909123456");
            emp1.setIsAdmin(true);
            emp1.setIsActivity(true);
            sampleEmployees.add(emp1);
            
            NhanVien emp2 = new NhanVien();
            emp2.setMaNV("NV002");
            emp2.setTenNV("Trần Thị Bình");
            emp2.setEmail("ttbinh@gmail.com");
            emp2.setSoDT("0912345678");
            emp2.setIsAdmin(false);
            emp2.setIsActivity(true);
            sampleEmployees.add(emp2);
            
            model.addAttribute("employees", sampleEmployees);
            model.addAttribute("message", "Database error: " + e.getMessage() + ". Using sample data.");
        }
        
        return "admin/employee-management";
    }
} 