package com.poly.controller.admin;

import com.poly.entity.KhachHang;
import com.poly.entity.TaiKhoan;
import com.poly.service.KhachHangService;
import com.poly.service.TaiKhoanService;
import com.poly.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/management/customer")
public class CustomerManagementController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private CodeGenerator codeGenerator;

    @GetMapping
    public String listCustomers(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> customers = khachHangService.findAll(pageable);

        model.addAttribute("customers", customers.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("totalItems", customers.getTotalElements());

        return "admin/show-customer";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new KhachHang());
        return "admin/edit-customer";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute KhachHang customer,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra email đã tồn tại chưa
            if (customer.getMaKH() == null || customer.getMaKH().isEmpty()) {
                // Tạo mới khách hàng
                if (khachHangService.findByEmail(customer.getEmail()).isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
                    return "redirect:/admin/management/customer/create";
                }

                // Sử dụng CustomerCodeGenerator để tạo mã khách hàng
                String maKH = codeGenerator.generateCustomerCode();
                customer.setMaKH(maKH);
            } else {
                // Cập nhật khách hàng
                Optional<KhachHang> existingCustomer = khachHangService.findById(customer.getMaKH());
                if (existingCustomer.isPresent()) {
                    KhachHang existing = existingCustomer.get();
                    if (!existing.getEmail().equals(customer.getEmail()) &&
                            khachHangService.findByEmail(customer.getEmail()).isPresent()) {
                        redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
                        return "redirect:/admin/management/customer/edit/" + customer.getMaKH();
                    }
                }
            }

            // Lưu khách hàng
            khachHangService.save(customer);

            // Nếu có thông tin tài khoản, tạo hoặc cập nhật tài khoản
            if (username != null && !username.isEmpty()) {
                Optional<TaiKhoan> existingAccount = taiKhoanService.findByTenTK(username);
                if (existingAccount.isPresent()) {
                    // Cập nhật tài khoản hiện có
                    TaiKhoan account = existingAccount.get();
                    if (password != null && !password.isEmpty()) {
                        account.setMatKhau(password);
                    }
                    account.setKhachHang(customer);
                    taiKhoanService.save(account);
                } else {
                    // Tạo tài khoản mới
                    TaiKhoan newAccount = new TaiKhoan();
                    newAccount.setTenTK(username);
                    newAccount.setMatKhau(password != null ? password : "123456"); // Mật khẩu mặc định
                    newAccount.setKhachHang(customer);
                    taiKhoanService.save(newAccount);
                }
            }

            redirectAttributes.addFlashAttribute("success", "Lưu khách hàng thành công!");
            return "redirect:/admin/management/customer";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/admin/management/customer";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<KhachHang> customerOpt = khachHangService.findById(id);
        if (customerOpt.isPresent()) {
            KhachHang customer = customerOpt.get();
            model.addAttribute("customer", customer);

            // Lấy thông tin tài khoản nếu có
            if (customer.getTaiKhoan() != null) {
                model.addAttribute("username", customer.getTaiKhoan().getTenTK());
            }

            return "admin/edit-customer";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng!");
            return "redirect:/admin/management/customer";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Optional<KhachHang> customerOpt = khachHangService.findById(id);
            if (customerOpt.isPresent()) {
                KhachHang customer = customerOpt.get();

                // Xóa tài khoản liên kết nếu có
                if (customer.getTaiKhoan() != null) {
                    taiKhoanService.deleteById(customer.getTaiKhoan().getId());
                }

                // Xóa khách hàng
                khachHangService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa khách hàng!");
        }
        return "redirect:/admin/management/customer";
    }
}