package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientDemoController {
    @GetMapping("/client/about")
    public String about() {
        return "Client/demo-fashion-store-about";
    }

    @GetMapping("/client/account")
    public String account() {
        return "Client/demo-fashion-store-account";
    }

    @GetMapping("/client/blog-single-creative")
    public String blogSingleCreative() {
        return "Client/demo-fashion-store-blog-single-creative";
    }

    @GetMapping("/client/cart")
    public String cart() {
        return "Client/demo-fashion-store-cart";
    }

    @GetMapping("/client/checkout")
    public String checkout() {
        return "Client/demo-fashion-store-checkout";
    }

    @GetMapping("/client/collection")
    public String collection() {
        return "Client/demo-fashion-store-collection";
    }

    @GetMapping("/client/contact")
    public String contact() {
        return "Client/demo-fashion-store-contact";
    }

    @GetMapping("/client/faq")
    public String faq() {
        return "Client/demo-fashion-store-faq";
    }

    @GetMapping("/client/magazine")
    public String magazine() {
        return "Client/demo-fashion-store-magazine";
    }

    @GetMapping("/client/no-sidebar")
    public String noSidebar() {
        return "Client/demo-fashion-store-no-sidebar";
    }

    @GetMapping("/client/right-sidebar")
    public String rightSidebar() {
        return "Client/demo-fashion-store-right-sidebar";
    }

    @GetMapping("/client/shop")
    public String shop() {
        return "Client/demo-fashion-store-shop";
    }

    @GetMapping("/client/single-product")
    public String singleProduct() {
        return "Client/demo-fashion-store-single-product";
    }

    @GetMapping("/client/wishlist")
    public String wishlist() {
        return "Client/demo-fashion-store-wishlist";
    }

    @GetMapping("/client/home")
    public String home() {
        return "Client/demo-fashion-store";
    }
} 