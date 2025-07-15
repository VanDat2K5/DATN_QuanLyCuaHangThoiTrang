package com.poly.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

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

//    // Trang shop
//    @GetMapping("/shop")
//    public String publicShop(HttpSession session, Model model) {
//        if (session.getAttribute("user") != null) {
//            model.addAttribute("user", session.getAttribute("user"));
//            model.addAttribute("username", session.getAttribute("username"));
//            model.addAttribute("userRole", session.getAttribute("userRole"));
//        }
//        return "Client/demo-fashion-store-shop";
//    }

    // Trang collection
    @GetMapping("/collection")
    public String publicCollection(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("userRole", session.getAttribute("userRole"));
        }
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
}