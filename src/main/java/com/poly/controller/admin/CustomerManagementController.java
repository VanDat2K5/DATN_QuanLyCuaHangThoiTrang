package com.poly.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

import com.poly.entity.KhachHang;
import com.poly.entity.HoaDon;
import com.poly.service.KhachHangService;
import com.poly.service.HoaDonService;
import com.poly.util.CodeGenerator;

@Controller
public class CustomerManagementController {

    @Autowired
    private final KhachHangService khachHangService;

    @Autowired
    private final HoaDonService hoaDonService;

    @Autowired
    private final CodeGenerator codeGenerator;

    public CustomerManagementController(KhachHangService khachHangService, HoaDonService hoaDonService, CodeGenerator codeGenerator) {
        this.khachHangService = khachHangService;
        this.hoaDonService = hoaDonService;
        this.codeGenerator = codeGenerator;
    }

    // Mapping cho URL cũ
    @GetMapping("/admin/customer-management")
    public String customerManagementOld(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return customerManagement(model, page, size);
    }

    // Mapping cho URL mới
    @GetMapping("/admin/management/customer")
    public String customerManagement(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> pageKhachHang = khachHangService.findAll(pageable);
        model.addAttribute("page", pageKhachHang);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "admin/customer-management";
    }

    @GetMapping("/admin/management/customer/search")
    public String searchCustomer(@RequestParam String keyword, Model model,
                                @RequestParam(defaultValue = "0") int page, 
                                @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> khachHang = khachHangService.findByMaKHContainingIgnoreCase(keyword, pageable);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("page", khachHang);
        return "admin/customer-management";
    }

    @PostMapping("/admin/management/customer/add")
    public String addCustomer(@ModelAttribute KhachHang khachHang, RedirectAttributes redirectAttributes) {
        try {
            if (khachHang.getTenKH() == null || khachHang.getTenKH().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên khách hàng không được để trống!");
                return "redirect:/admin/management/customer";
            }
            
            if (khachHang.getEmail() == null || khachHang.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email không được để trống!");
                return "redirect:/admin/management/customer";
            }
            
            // Kiểm tra email đã tồn tại chưa
            if (khachHangService.findByEmail(khachHang.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
                return "redirect:/admin/management/customer";
            }
            
            // Tự động tạo mã khách hàng
            String maKH = codeGenerator.generateCustomerCode();
            khachHang.setMaKH(maKH);
            
            khachHangService.save(khachHang);
            redirectAttributes.addFlashAttribute("success", "Thêm khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm khách hàng: " + e.getMessage());
        }
        return "redirect:/admin/management/customer";
    }

    @PostMapping("/admin/management/customer/edit/{maKH}")
    public String editCustomer(@PathVariable String maKH, @ModelAttribute KhachHang khachHang, RedirectAttributes redirectAttributes) {
        try {
            // Validation cơ bản
            if (khachHang.getTenKH() == null || khachHang.getTenKH().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên khách hàng không được để trống!");
                return "redirect:/admin/management/customer";
            }
            
            if (khachHang.getEmail() == null || khachHang.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email không được để trống!");
                return "redirect:/admin/management/customer";
            }
            
            // Kiểm tra email đã tồn tại chưa (trừ khách hàng hiện tại)
            Optional<KhachHang> existingCustomer = khachHangService.findByEmail(khachHang.getEmail());
            if (existingCustomer.isPresent() && !existingCustomer.get().getMaKH().equals(maKH)) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
                return "redirect:/admin/management/customer";
            }
            
            khachHang.setMaKH(maKH);
            khachHangService.save(khachHang);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
        return "redirect:/admin/management/customer";
    }

    @PostMapping("/admin/management/customer/delete/{maKH}")
    public String deleteCustomer(@PathVariable String maKH, RedirectAttributes redirectAttributes) {
        try {
            khachHangService.deleteById(maKH);
            redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa khách hàng: " + e.getMessage());
        }
        return "redirect:/admin/management/customer";
    }

    @GetMapping("/admin/management/customer/{id}")
    public String customerDetail(@PathVariable String id, Model model,
                                @RequestParam(defaultValue = "0") int page, 
                                @RequestParam(defaultValue = "5") int size) {
        Optional<KhachHang> khachHang = khachHangService.findById(id);
        if (khachHang.isPresent()) {
            model.addAttribute("khachHang", khachHang.get());
            Pageable pageable = PageRequest.of(page, size);
            Page<HoaDon> hoaDon = hoaDonService.findByKhachHang_MaKH(khachHang.get().getMaKH(), pageable);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("hoaDon", hoaDon);
            return "admin/customer-detail";
        } else {
            return "redirect:/admin/management/customer";
        }
    }
}