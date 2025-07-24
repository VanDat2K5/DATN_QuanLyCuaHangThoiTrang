package com.poly.controller.admin;

import com.poly.entity.NhanVien;
import com.poly.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/management/employee")
public class EmployeeManagementController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping
    public String employeeManagement(Model model) {
        List<NhanVien> employees = nhanVienService.findAll();
        model.addAttribute("employees", employees);
        return "admin/employee-management";
    }

    @GetMapping("/create")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new NhanVien());
        return "admin/employee-form";
    }

    @PostMapping("/create")
    public String addEmployee(@ModelAttribute NhanVien nhanVien, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra email đã tồn tại chưa
            if (nhanVienService.findByEmail(nhanVien.getEmail()).size() > 0) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
                return "redirect:/admin/management/employee/create";
            }

            // Kiểm tra số điện thoại đã tồn tại chưa
            if (nhanVienService.findBySoDT(nhanVien.getSoDT()).size() > 0) {
                redirectAttributes.addFlashAttribute("error", "Số điện thoại đã tồn tại!");
                return "redirect:/admin/management/employee/create";
            }

            nhanVienService.save(nhanVien);
            redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/management/employee";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        NhanVien employee = nhanVienService.findById(id).orElse(null);
        if (employee == null) {
            return "redirect:/admin/management/employee";
        }
        model.addAttribute("employee", employee);
        return "admin/employee-form";
    }

    @PostMapping("/edit")
    public String editEmployee(@ModelAttribute NhanVien nhanVien, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra email đã tồn tại chưa (trừ nhân viên hiện tại)
            List<NhanVien> existingByEmail = nhanVienService.findByEmail(nhanVien.getEmail());
            if (existingByEmail.size() > 0 && !existingByEmail.get(0).getMaNV().equals(nhanVien.getMaNV())) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
                return "redirect:/admin/management/employee/edit/" + nhanVien.getMaNV();
            }

            // Kiểm tra số điện thoại đã tồn tại chưa (trừ nhân viên hiện tại)
            List<NhanVien> existingByPhone = nhanVienService.findBySoDT(nhanVien.getSoDT());
            if (existingByPhone.size() > 0 && !existingByPhone.get(0).getMaNV().equals(nhanVien.getMaNV())) {
                redirectAttributes.addFlashAttribute("error", "Số điện thoại đã tồn tại!");
                return "redirect:/admin/management/employee/edit/" + nhanVien.getMaNV();
            }

            nhanVienService.save(nhanVien);
            redirectAttributes.addFlashAttribute("success", "Cập nhật nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/management/employee";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            nhanVienService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/management/employee";
    }

    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            NhanVien employee = nhanVienService.findById(id).orElse(null);
            if (employee != null) {
                employee.setIsActivity(!employee.getIsActivity());
                nhanVienService.save(employee);
                String status = employee.getIsActivity() ? "kích hoạt" : "khóa";
                redirectAttributes.addFlashAttribute("success", "Đã " + status + " nhân viên thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/management/employee";
    }
}