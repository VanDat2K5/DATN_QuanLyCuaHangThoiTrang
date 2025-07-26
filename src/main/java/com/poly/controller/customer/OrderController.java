package com.poly.controller.customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.entity.ChiTietHoaDon;
import com.poly.entity.ChiTietSanPham;
import com.poly.entity.DiaChiNhanHang;
import com.poly.entity.HoaDon;
import com.poly.entity.KhachHang;
import com.poly.service.ChiTietHoaDonService;
import com.poly.service.ChiTietSanPhamService;
import com.poly.service.DiaChiNhanHangService;
import com.poly.service.HoaDonService;
import com.poly.util.CodeGenerator;
import com.poly.util.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private CodeGenerator codeGenerator;

	@Autowired
	private DiaChiNhanHangService diaChiNhanHangService;

	@Autowired
	private HoaDonService hoaDonService;

	@Autowired
	private ChiTietHoaDonService chiTietHoaDonService;

	@Autowired
	private ChiTietSanPhamService chiTietSanPhamService;

	@Autowired
	private EmailService emailService;

	@GetMapping("/checkout")
	public String checkout(Model model, HttpSession session) {
		KhachHang user = (KhachHang) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}

		List<DiaChiNhanHang> addressList = diaChiNhanHangService.findByKhachHang_MaKH(user.getMaKH());
		model.addAttribute("user", user);
		model.addAttribute("addressList", addressList);
		return "Client/order-checkout";
	}

	@PostMapping("/confirm")
	public ResponseEntity<?> confirmOrder(@RequestBody Map<String, Object> requestData, HttpSession session) {
		try {
			KhachHang user = (KhachHang) session.getAttribute("user");
			if (user == null) {
				return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Chưa đăng nhập"));
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> addressMap = (Map<String, Object>) requestData.get("address");
			String paymentMethod = (String) requestData.get("paymentMethod");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> cartItems = (List<Map<String, Object>>) requestData.get("cartItems");

			if (addressMap == null || addressMap.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Thiếu thông tin địa chỉ"));
			}

			// Xử lý địa chỉ
			DiaChiNhanHang diaChiNhanHang = null;
			boolean isExistingAddress = false;

			if (addressMap.containsKey("id")) {
				Object idObj = addressMap.get("id");
				if (idObj != null && !idObj.toString().trim().isEmpty()) {
					try {
						String addressId = idObj.toString().trim();
						Optional<DiaChiNhanHang> optionalAddress = diaChiNhanHangService
								.findById(Integer.parseInt(addressId));
						if (optionalAddress.isPresent()) {
							diaChiNhanHang = optionalAddress.get();
							isExistingAddress = true;
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid address ID format: " + idObj);
					}
				}
			}

			if (!isExistingAddress) {
				Object tenNguoiNhanObj = addressMap.get("tenNguoiNhan");
				Object soDTNhanHangObj = addressMap.get("soDTNhanHang");
				Object dcNhanHangObj = addressMap.get("dcNhanHang");

				String tenNguoiNhan = tenNguoiNhanObj != null ? tenNguoiNhanObj.toString().trim() : "";
				String soDTNhanHang = soDTNhanHangObj != null ? soDTNhanHangObj.toString().trim() : "";
				String dcNhanHang = dcNhanHangObj != null ? dcNhanHangObj.toString().trim() : "";

				if (tenNguoiNhan.isEmpty() || soDTNhanHang.isEmpty() || dcNhanHang.isEmpty()) {
					return ResponseEntity.badRequest()
							.body(Map.of("success", false, "error", "Thiếu thông tin địa chỉ giao hàng"));
				}

				diaChiNhanHang = new DiaChiNhanHang();
				diaChiNhanHang.setKhachHang(user);
				diaChiNhanHang.setTenNguoiNhan(tenNguoiNhan);
				diaChiNhanHang.setSoDTNhanHang(soDTNhanHang);
				diaChiNhanHang.setDcNhanHang(dcNhanHang);
				diaChiNhanHangService.save(diaChiNhanHang);
			}

			// Tạo hóa đơn
			HoaDon hoaDon = new HoaDon();
			hoaDon.setMaHD(codeGenerator.generateOrderCode());
			hoaDon.setKhachHang(user);
			hoaDon.setNgayLap(LocalDate.now());
			hoaDon.setTrangThai("ChoXacNhan");
			hoaDon.setTenNguoiNhan(diaChiNhanHang.getTenNguoiNhan());
			hoaDon.setSoDTNhanHang(diaChiNhanHang.getSoDTNhanHang());
			hoaDon.setDcNhanHang(diaChiNhanHang.getDcNhanHang());
			hoaDon.setPtThanhToan(paymentMethod);
			hoaDon.setTongTien(BigDecimal.ZERO);

			hoaDonService.save(hoaDon);

			BigDecimal tongTien = BigDecimal.ZERO;

			// Tạo chi tiết hóa đơn
			for (Map<String, Object> item : cartItems) {
				Object tenSPObj = item.get("tenSP");
				Object maMauObj = item.get("mau");
				Object maSizeObj = item.get("size");
				Object soLuongObj = item.get("SoLuong");
				Object giaObj = item.get("Gia");

				if (tenSPObj == null || maMauObj == null || maSizeObj == null || soLuongObj == null || giaObj == null) {
					continue;
				}

				String tenSP = tenSPObj.toString();
				String maMau = maMauObj.toString();
				String maSize = maSizeObj.toString();
				int soLuong = Integer.parseInt(soLuongObj.toString());
				BigDecimal gia = new BigDecimal(giaObj.toString());

				Optional<ChiTietSanPham> optionalCTSP = chiTietSanPhamService
						.findBySanPham_TenSPAndMau_MaMauAndSize_MaSize(tenSP, maMau, maSize);

				if (optionalCTSP.isPresent()) {
					ChiTietSanPham ctsp = optionalCTSP.get();

					ChiTietHoaDon cthd = new ChiTietHoaDon();
					cthd.setHoaDon(hoaDon);
					cthd.setChiTietSanPham(ctsp);
					cthd.setGiaXuat(gia);
					cthd.setSoLuongXuat(soLuong);
					cthd.setLoHang("LOT001");
					cthd.setThanhTien(gia.multiply(BigDecimal.valueOf(soLuong)));

					chiTietHoaDonService.save(cthd);
					tongTien = tongTien.add(cthd.getThanhTien());
				}
			}

			// Cập nhật tổng tiền cho hóa đơn
			hoaDon.setTongTien(tongTien);
			hoaDonService.save(hoaDon);

			// Gửi email xác nhận
			try {
				emailService.sendOrderConfirmation(user, hoaDon);
			} catch (Exception e) {
				System.out.println("Lỗi gửi email: " + e.getMessage());
			}

			// Trả về thông tin QR code nếu thanh toán bằng Bank
			if ("Bank".equals(paymentMethod)) {
				String qrCodeData = "https://api.vietqr.io/image/VCB-1234567890-1000000-" + hoaDon.getMaHD();
				return ResponseEntity.ok(Map.of(
						"success", true,
						"orderId", hoaDon.getMaHD(),
						"qrCode", qrCodeData,
						"totalAmount", tongTien,
						"paymentContent", "Thanh toan don hang " + hoaDon.getMaHD()));
			} else {
				return ResponseEntity.ok(Map.of(
						"success", true,
						"orderId", hoaDon.getMaHD()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Map.of("success", false, "error", "Lỗi xử lý đơn hàng: " + e.getMessage()));
		}
	}

	@GetMapping("/check-payment/{orderId}")
	public ResponseEntity<?> checkPaymentStatus(@PathVariable String orderId) {
		try {
			Optional<HoaDon> optionalOrder = hoaDonService.findById(orderId);
			if (optionalOrder.isPresent()) {
				HoaDon order = optionalOrder.get();
				// Kiểm tra trạng thái thanh toán (có thể tích hợp với API ngân hàng)
				// Hiện tại trả về false để demo
				boolean isPaid = false;
				return ResponseEntity.ok(Map.of("isPaid", isPaid));
			} else {
				return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy đơn hàng"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Lỗi kiểm tra thanh toán"));
		}
	}

	@GetMapping("/success/{orderId}")
	public String orderSuccess(@PathVariable String orderId, Model model) {
		Optional<HoaDon> optionalOrder = hoaDonService.findById(orderId);
		if (optionalOrder.isPresent()) {
			HoaDon order = optionalOrder.get();
			// Load chi tiết hóa đơn
			List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDonService.findByHoaDon_MaHD(orderId);
			model.addAttribute("order", order);
			model.addAttribute("chiTietHoaDons", chiTietHoaDons);
			return "Client/order-success";
		} else {
			return "redirect:/";
		}
	}
}
