package com.poly.controller;

import com.poly.entity.SanPham;
import com.poly.service.SanPhamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PublicController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/")
    public String publicHome(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store";
    }

    @GetMapping("/about")
    public String publicAbout(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-about";
    }

    @GetMapping("/contact")
    public String publicContact(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-contact";
    }

    @GetMapping("/faq")
    public String publicFaq(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-faq";
    }

    // Trang tạp chí
    @GetMapping("/magazine")
    public String publicMagazine(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-magazine";
    }

//    // Trang shop với lọc theo giới tính
//    @GetMapping("/shop")
//    public String publicShop(HttpSession session, Model model,
//            @RequestParam(value = "gender", required = false) String gender) {
//        if (session.getAttribute("user") != null) {
//            model.addAttribute("user", session.getAttribute("user"));
//            model.addAttribute("username", session.getAttribute("username"));
//            model.addAttribute("userRole", session.getAttribute("userRole"));
//        }
//
//        List<SanPham> products;
//        if (gender != null && !gender.isEmpty()) {
//            try {
//                SanPham.Gender genderEnum = SanPham.Gender.valueOf(gender.toUpperCase());
//                products = sanPhamService.findByGioiTinh(genderEnum);
//                model.addAttribute("selectedGender", genderEnum);
//            } catch (IllegalArgumentException e) {
//                products = sanPhamService.findAll();
//            }
//        } else {
//            products = sanPhamService.findAll();
//        }
//
//        model.addAttribute("products", products);
//        model.addAttribute("genders", SanPham.Gender.values());
//        return "Client/demo-fashion-store-shop";
//    }

    // Trang collection với lọc theo giới tính
    @GetMapping("/collection")
    public String publicCollection(HttpSession session, Model model,
            @RequestParam(value = "gender", required = false) String gender) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }

        List<SanPham> products;
        if (gender != null && !gender.isEmpty()) {
            try {
                SanPham.Gender genderEnum = SanPham.Gender.valueOf(gender.toUpperCase());
                products = sanPhamService.findByGioiTinh(genderEnum);
                model.addAttribute("selectedGender", genderEnum);
            } catch (IllegalArgumentException e) {
                products = sanPhamService.findAll();
            }
        } else {
            products = sanPhamService.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("genders", SanPham.Gender.values());
        return "Client/demo-fashion-store-collection";
    }

    // Trang chi tiết sản phẩm
    @GetMapping("/product")
    public String publicProduct(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
        return "Client/demo-fashion-store-single-product";
    }

    // API endpoint để lọc sản phẩm theo giới tính (cho AJAX)
    @GetMapping("/api/products/by-gender")
    public String getProductsByGender(@RequestParam("gender") String gender, Model model) {
        try {
            SanPham.Gender genderEnum = SanPham.Gender.valueOf(gender.toUpperCase());
            List<SanPham> products = sanPhamService.findByGioiTinh(genderEnum);
            model.addAttribute("products", products);
            model.addAttribute("selectedGender", genderEnum);
        } catch (IllegalArgumentException e) {
            List<SanPham> products = sanPhamService.findAll();
            model.addAttribute("products", products);
        }

        model.addAttribute("genders", SanPham.Gender.values());
        return "Client/fragments/product-list"; // Fragment để render lại danh sách sản phẩm
    }
}