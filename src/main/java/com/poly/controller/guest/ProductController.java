package com.poly.controller.guest;

import com.poly.dto.SanPhamViewDTO;
import com.poly.entity.SanPham;
import com.poly.service.SanPhamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ProductController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public String listProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("tenSP").ascending());
        Page<SanPhamViewDTO> sanPhamPage = sanPhamService.findAllSanPhamDTO(pageable);

        model.addAttribute("products", sanPhamPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "Client/demo-fashion-store-shop";
    }
}
