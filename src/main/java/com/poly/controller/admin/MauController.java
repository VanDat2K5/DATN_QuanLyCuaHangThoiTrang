package com.poly.controller.admin;

import com.poly.entity.Mau;
import com.poly.service.MauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/management/color")
public class MauController {

    @Autowired
    private MauService mauService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", mauService.findAll());
        return "admin/color/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("color", new Mau());
        return "admin/color/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Mau mau) {
        mauService.save(mau);
        return "redirect:/admin/management/color";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("color", mauService.findById(id).orElseThrow());
        return "color/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        mauService.deleteById(id);
        return "redirect:/admin/management/color";
    }
}