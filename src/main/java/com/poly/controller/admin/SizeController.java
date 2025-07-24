package com.poly.controller.admin;

import com.poly.entity.Size;
import com.poly.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/management/size")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    // 1. Danh sách Size
    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", sizeService.findAll());
        return "admin/size/list"; // tương ứng templates/admin/size/list.html
    }

    // 2. Form thêm mới
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("size", new Size());
        return "admin/size/form"; // tương ứng templates/admin/size/form.html
    }

    // 3. Lưu (thêm mới hoặc cập nhật)
    @PostMapping("/save")
    public String save(@ModelAttribute("size") Size size) {
        sizeService.save(size);
        return "redirect:/admin/management/size";
    }

    // 4. Form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        Size size = sizeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Size với id: " + id));
        model.addAttribute("size", size);
        return "admin/size/form";
    }

    // 5. Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        sizeService.deleteById(id);
        return "redirect:/admin/management/size";
    }
}
