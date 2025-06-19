package com.poly.controller;

import com.poly.util.QRCodeServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QRCodeTestController {
    @Autowired
    private QRCodeServlet qrCodeServlet;

    @GetMapping("/test-qr")
    public String testQRPage() {
        return "test-qr";
    }

    @PostMapping("/test-qr")
    @ResponseBody
    public Map<String, String> testQR(@RequestBody Map<String, String> body) {
        String tien = body.getOrDefault("tien", "");
        String noidung = body.getOrDefault("noidung", "");
        String qrBase64 = qrCodeServlet.generateQRCodeBase64(tien, noidung);
        Map<String, String> result = new HashMap<>();
        result.put("qrBase64", qrBase64);
        return result;
    }
}