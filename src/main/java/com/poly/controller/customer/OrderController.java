package com.poly.controller.customer;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.entity.HoaDon;
import com.poly.entity.KhachHang;
import com.poly.util.CodeGenerator;

import jakarta.servlet.http.HttpSession;

public class OrderController {

	@Autowired
	private CodeGenerator codeGenerator;

	@GetMapping("/create")
	public String showCreateOrder(Model model, HttpSession session) {
		KhachHang khachHang = (KhachHang) session.getAttribute("user");
		HoaDon hd = new HoaDon();
		hd.setMaHD(codeGenerator.generateOrderCode());
		hd.setKhachHang(khachHang);
		hd.setNgayLap(LocalDate.now());
		model.addAttribute("orders", hd);
		return "/";
	}
	
	@GetMapping("/1")
	public String show(Model model, HttpSession session) {
		
		return "/admin/show-customer";
	}
	
}
