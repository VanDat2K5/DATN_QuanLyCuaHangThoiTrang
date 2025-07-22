package com.poly.controller.customer;

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

    @GetMapping("/filter")
    public String publicShop(HttpSession session, Model model,
            @RequestParam(value = "gender", required = false) String gender) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }

        List<SanPhamViewDTO> products;

        if (gender != null && !gender.isEmpty()) {
            try {
                SanPham.Gender genderEnum = SanPham.Gender.valueOf(gender.toUpperCase());
                products = sanPhamService.findByGioiTinh(genderEnum);
                model.addAttribute("selectedGender", genderEnum);
            } catch (IllegalArgumentException e) {
                products = sanPhamService.findAll()
                        .stream()
                        .map(SanPhamViewDTO::new)
                        .toList();
            }
        } else {
            products = sanPhamService.findAll()
                    .stream()
                    .map(SanPhamViewDTO::new)
                    .toList();
        }

        model.addAttribute("products", products);
        model.addAttribute("genders", SanPham.Gender.values());
        return "Client/demo-fashion-store-shop";
    }
}
