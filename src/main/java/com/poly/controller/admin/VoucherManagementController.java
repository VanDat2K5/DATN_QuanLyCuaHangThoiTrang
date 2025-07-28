package com.poly.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

import com.poly.entity.KhuyenMai;
import com.poly.service.KhuyenMaiService;

@Controller
public class VoucherManagementController {

    @Autowired
    private final KhuyenMaiService khuyenMaiService;

    public VoucherManagementController(KhuyenMaiService khuyenMaiService) {
        this.khuyenMaiService = khuyenMaiService;
    }

    // Mapping cho URL cũ
    @GetMapping("/admin/voucher-management")
    public String voucherManagementOld(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return voucherManagement(model, page, size);
    }

    // Mapping cho URL mới
    @GetMapping("/admin/management/voucher")
    public String voucherManagement(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhuyenMai> pageKhuyenMai = khuyenMaiService.findAll(pageable);
        model.addAttribute("page", pageKhuyenMai);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "admin/voucher-management";
    }

    @GetMapping("/admin/management/voucher/search")
    public String searchVoucher(@RequestParam String keyword, Model model,
                                @RequestParam(defaultValue = "0") int page, 
                                @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhuyenMai> khuyenMai = khuyenMaiService.findByMaKMContainingIgnoreCase(keyword, pageable);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("page", khuyenMai);
        return "admin/voucher-management";
    }

    @PostMapping("/admin/management/voucher/add")
    public String addVoucher(@ModelAttribute KhuyenMai khuyenMai, RedirectAttributes redirectAttributes) {
        try {
            khuyenMaiService.save(khuyenMai);
            redirectAttributes.addFlashAttribute("success", "Thêm voucher thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm voucher: " + e.getMessage());
        }
        return "redirect:/admin/management/voucher";
    }

    @PostMapping("/admin/management/voucher/edit/{maKM}")
    public String editVoucher(@PathVariable String maKM, @ModelAttribute KhuyenMai khuyenMai, RedirectAttributes redirectAttributes) {
        try {
            khuyenMai.setMaKM(maKM);
            khuyenMaiService.save(khuyenMai);
            redirectAttributes.addFlashAttribute("success", "Cập nhật voucher thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật voucher: " + e.getMessage());
        }
        return "redirect:/admin/management/voucher";
    }

    @PostMapping("/admin/management/voucher/delete/{maKM}")
    public String deleteVoucher(@PathVariable String maKM, RedirectAttributes redirectAttributes) {
        try {
            khuyenMaiService.deleteById(maKM);
            redirectAttributes.addFlashAttribute("success", "Xóa voucher thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa voucher: " + e.getMessage());
        }
        return "redirect:/admin/management/voucher";
    }

    @GetMapping("/admin/management/voucher/{id}")
    public String voucherDetail(@PathVariable String id, Model model) {
        Optional<KhuyenMai> khuyenMai = khuyenMaiService.findById(id);
        if (khuyenMai.isPresent()) {
            model.addAttribute("voucher", khuyenMai.get());
            return "admin/voucher-detail";
        } else {
            return "redirect:/admin/management/voucher";
        }
    }
} 