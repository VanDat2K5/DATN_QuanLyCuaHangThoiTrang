package com.poly.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.poly.entity.ChiTietHoaDon;
import com.poly.entity.HoaDon;
import com.poly.entity.KhachHang;
import com.poly.service.ChiTietHoaDonService;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ChiTietHoaDonService chiTietHoaDonService;

	public void sendOrderConfirmation(KhachHang user, HoaDon orders) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setSubject("Xác Nhận Đơn Hàng #" + orders.getMaHD());

			StringBuilder emailContent = new StringBuilder();

			String bank = "Bank".equals(orders.getPtThanhToan()) ? "Chuyển khoản" : "COD (Thanh toán khi nhận hàng)";

			emailContent.append("<html><body>");
			emailContent.append(
					"<div id=\":z7\" class=\"ii gt\" jslog=\"20277; u014N:xr6bB; 1:WyIjdGhyZWFkLWY6MTgyNzQ0MTQ3NDM5ODc4NTkwNiJd; 4:WyIjbXNnLWY6MTgyNzQ0MTQ3NDM5ODc4NTkwNiIsbnVsbCxudWxsLG51bGwsMSwwLFsxLDAsMF0sMTUzLDgxMixudWxsLG51bGwsbnVsbCxudWxsLG51bGwsMSxudWxsLG51bGwsWzNdLG51bGwsbnVsbCxudWxsLG51bGwsbnVsbCxudWxsLDBd\"><div id=\":z6\" class=\"a3s aiL \"><div class=\"adM\">\r\n"
							+ "<div style=\"font-family:'Quicksand','Montserrat',Arial,sans-serif;line-height:1.6;padding:35px 35px;font-size:17px;max-width:650px;margin:0 auto;font-weight:500;background-color:#f9f9f9;color:#333333\"><div class=\"adM\">"
							+ "</div><div style=\"text-align:center;padding:20px 10px;background-color:#edebeb; font-weight: bold;  font-size: 35px;\">StyleTech Store<div class=\"adM\">\r\n"
							+ "</div>"
							+ "		<div style=\"background-color:#28a745;padding:15px 15px;text-align:center;color:white;margin-bottom:0px;border-radius:8px 8px 0 0; margin-top: 10px;\">\r\n"
							+ "        <h1 style=\"margin:0;font-size:25px;font-weight:700;letter-spacing:0.5px\">Thông báo đặt hàng thành công</h1>\r\n"
							+ "     </div>"
							+ "<div style=\"text-align: left;\">\r\n");
			emailContent.append(
					"<p style=\"margin-top:5px;font-size:18px;font-weight:500\">Cảm ơn bạn <span style=\"font-weight:700;color:#222222\">"
							+ user.getTenKH() + "!</span>,</p>");
			emailContent.append(
					"<p style=\"font-size:18px;font-weight:500\">Đơn hàng <strong style=\"color:#222222;font-weight:700\"> #"
							+ orders.getMaHD()
							+ "</strong> của bạn tại <strong>StyleTech</strong> đã được xác nhận.</p>");
			emailContent.append("\r\n"
					+ "            <div style=\"margin-top:30px;background-color:#f6f6f6;padding:20px;border-radius:8px;border-left:4px solid #3d97e5\">\r\n"
					+ "                <p style=\"font-weight:700;font-size:18px;margin-top:0;margin-bottom:12px;color:#222222\">Thông tin giao hàng:</p>\r\n"
					+ "                \r\n"
					+ "                <div style=\"font-size:17px;color:#333333;font-weight:500;margin-bottom:10px\">\r\n"
					+ "                    <strong style=\"color:#222222\">Mã đơn hàng:</strong> " + orders.getMaHD()
					+ "\r\n"
					+ "                </div>\r\n"
					+ "                <div style=\"font-size:17px;color:#333333;font-weight:500;margin-bottom:10px\">\r\n"
					+ "                    <strong style=\"color:#222222\">Ngày tạo giao hàng:</strong> "
					+ formatDate(orders.getNgayLap()) + "\r\n"
					+ "                </div>\r\n"
					+ "                <div style=\"font-size:17px;color:#333333;font-weight:500;margin-bottom:10px\">\r\n"
					+ "                    <strong style=\"color:#222222\">Phương thức thanh toán:</strong> " + bank
					+ "\r\n"
					+ "                </div>\r\n"
					+ "                <div style=\"font-size:17px;color:#333333;font-weight:500\">\r\n"
					+ "                    <strong style=\"color:#222222\">Tổng giá trị đơn hàng: </strong>"
					+ formatPrice(orders.getTongTien()) + "\r\n"
					+ "                </div>\r\n"
					+ "            </div>");

			emailContent.append("\r\n"
					+ "				<div style=\"margin-top:25px;background-color:#fafafa;padding:20px;border-radius:8px;\">\r\n"
					+ "                <p style=\"font-weight:700;margin-top:0;margin-bottom:12px;color:#222222;font-size:18px;\">Thông tin người nhận:</p>\r\n"
					+ "                <table style=\"width:100%;font-size:16px;\">\r\n"
					+ "                    <tbody>\r\n"
					+ "                        <tr>\r\n"
					+ "                            <td style=\"width:30%;color:#222222;font-weight:600\">Họ tên:</td>\r\n"
					+ "                            <td>" + user.getTenKH() + "</td>\r\n"
					+ "                        </tr>\r\n"
					+ "                        <tr>\r\n"
					+ "                            <td style=\"color:#222222;font-weight:600\">Điện thoại:</td>\r\n"
					+ "                            <td>" + orders.getSoDTNhanHang() + "</td>\r\n"
					+ "                        </tr>\r\n"
					+ "                        <tr>\r\n"
					+ "                            <td style=\"color:#222222;font-weight:600\">Địa chỉ:</td>\r\n"
					+ "                            <td>" + orders.getDcNhanHang() + "</td>\r\n"
					+ "                        </tr>\r\n"
					+ "                    </tbody>\r\n"
					+ "                </table>\r\n"
					+ "            </div>");

			emailContent.append("\r\n"
					+ "            <div style=\"margin-top:25px\">\r\n"
					+ "                <p style=\"font-weight:700;font-size:18px;margin-bottom:15px;color:#222222\">Sản phẩm trong đơn hàng:</p>\r\n"
					+ "                <ul style=\"padding-left:0;list-style-type:none;margin-bottom:0\">\r\n"
					+ "                    <li style=\"margin-bottom:10px\">\r\n"
					+ "                        <table style=\"width:100%;border-bottom:1px solid #eaeaea;border-radius:6px;overflow:hidden;background-color:#fafafa\">\r\n"
					+ "                            <tbody>\r\n");

			for (ChiTietHoaDon product : chiTietHoaDonService.findByHoaDon_MaHD(orders.getMaHD())) {
				emailContent.append("\r\n"
						+ "                                <tr>\r\n"
						+ "                                    <td style=\"width:100%;padding:20px 15px 0px 15px\" colspan=\"2\">\r\n"
						+ "                                    <div style=\"float:left;width:80px;height:80px;border:1px solid #eaeaea;border-radius:4px;overflow:hidden;background-color:#ffffff\">\r\n"
						// + " <img style=\"max-width:100%;max-height:100%\"
						// src=\"https://ci3.googleusercontent.com/meips/ADKq_Nbkfmx1F4il1R6fQLm7FlDwo7nGClvaulYJJQcaS8Q-IJjWudjuAxU7Iqd4RZFhIZ2Qv0EhLTYZcWdFOIqlMEV1fxwf5w1lRQ9lwDMbrDFoHoKe0ERAhXfQcw6F13DerF8p3z0uuTm0MS_n4cQz_sdYJrJdsgYKyEOooeM76SLaEPRgmKmKBdkgiRtSBeSSzWMlAqWYXLqs=s0-d-e1-ft#https://bizweb.dktcdn.net/thumb/small/100/456/060/products/885f5b30-6c43-4a12-af2f-9221e289d241-1733299192261.jpg?v=1733299454493\"
						// class=\"CToWUd\" data-bit=\"iit\">\r\n"
						+ "                                    </div>\r\n"
						+ "                                    <div style=\"margin-left:100px\">\r\n"
						+ "                                        <span style=\"color:#333333;text-decoration:none;font-weight:700;font-size:17px\">"
						+ product.getChiTietSanPham().getSanPham().getTenSP() + "</span>\r\n"
						+ "                                        <p style=\"color:#555555;margin-bottom:0;margin-top:5px;font-size:15px;font-weight:500\">\r\n"
						+ "                                        Mặc định \r\n"
						+ "                                        </p>\r\n"
						+ "                                        <p style=\"color:#555555;margin-bottom:0;margin-top:5px;font-size:15px;font-weight:500\">Mã SP: "
						+ product.getChiTietSanPham().getSanPham().getMaSP() + "</p>\r\n"
						+ "                                    </div>\r\n"
						+ "                                    </td>\r\n"
						+ "                                </tr>\r\n"
						+ "                                <tr>\r\n"
						+ "                                    <td style=\"width:70%;padding:5px 15px 20px 15px\">\r\n"
						+ "                                    <div style=\"margin-left:100px;color:#333333;font-weight:500;font-size:16px\">\r\n"
						+ "                                        " + formatPrice(product.getGiaXuat())
						+ "                                        <span style=\"margin-left:20px\">x "
						+ product.getSoLuongXuat() + "</span>\r\n"
						+ " <span style=\"margin-left:20px\">x " + product.getSoLuongXuat() + "</span>\r\n"
						+ "                                    </div>\r\n"
						+ "                                    </td>\r\n"
						+ "                                    <td style=\"text-align:right;width:30%;padding:5px 15px 20px 15px;font-weight:700;color:#222222;font-size:16px\">\r\n"
						+ "                                    "
						+ formatPrice(product.getGiaXuat().multiply(BigDecimal.valueOf(product.getSoLuongXuat())))
						+ "</td>\r\n"
						+ "                                </tr>\r\n");
			}
			emailContent.append("\r\n"
					+ "                            </tbody>\r\n"
					+ "                        </table>\r\n"
					+ "                    </li>\r\n"
					+ "                </ul>\r\n"
					+ "            </div>");
			emailContent.append("\r\n"
					+ "            <div style=\"margin-top:30px;background-color:#f5f5f5;padding:20px;border-radius:8px;border-left:4px solid #3d97e5\">\r\n"
					+ "                <p style=\"font-weight:700;font-size:17px;margin-top:0;margin-bottom:12px;color:#222222\">Thông tin liên hệ:</p>\r\n"
					+ "                \r\n"
					+ "                <ul style=\"padding-left:15px;margin-bottom:0;margin-top:12px\">\r\n"
					+ "                  <li style=\"margin-bottom:10px;font-weight:600;color:#333333;font-size:16px\">\r\n"
					+ "                    <strong style=\"color:#222222\">Email:</strong> <a href=\"mailto:serversuperv001@gmail.com\" style=\"color:#3d97e5;text-decoration:none;font-weight:600\" target=\"_blank\">serversuperv001@gmail.com</a>\r\n"
					+ "                  </li>\r\n"
					+ "                  <li style=\"margin-bottom:0;font-weight:600;color:#333333;font-size:16px\">\r\n"
					+ "                    <strong style=\"color:#222222\">Zalo StyleTech:</strong> <a href=\"https://zalo.me/0855022430\" style=\"color:#3d97e5;text-decoration:none;font-weight:600\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://zalo.me/0961615021&amp;source=gmail&amp;ust=1744269869790000&amp;usg=AOvVaw1cgvXtts5BQG9adblxbuNi\">https://zalo.me/0855022430</a>\r\n"
					+ "                  </li>\r\n"
					+ "                </ul>\r\n"
					+ "            </div>\r\n"
					+ "            <div style=\"margin-top:30px;text-align:center;padding-top:20px;border-top:1px solid #eaeaea\">\r\n"
					+ "                <p style=\"text-align:center;font-size:17px;color:#333333;font-weight:500;margin-bottom:15px\">Shop rất mong sản phẩm sẽ đến tay bạn một cách nhanh nhất và an toàn nhất.</p>\r\n"
					+ "                <p style=\"text-align:center;color:#555555;font-style:italic;margin-bottom:5px;font-size:16px;font-weight:500\">Trân trọng,</p>\r\n"
					+ "                <p style=\"text-align:center;font-weight:700;color:#222222;margin-top:5px;font-size:19px\">StyleTech Store</p>\r\n"
					+ "            </div>\r\n"
					+ "        </div>\r\n"
					+ "    </div>");
			emailContent.append("</body></html>");

			helper.setText(emailContent.toString(), true);

			helper.setTo(user.getEmail());
			helper.setFrom("serversuperv001@gmail.com");
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendPasswordResetEmail(KhachHang user, String token) throws MessagingException {
		String resetLink = "http://localhost:8080/account/reset-password?token=" + token;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(user.getEmail());
		helper.setSubject("Yêu Cầu Reset Mật Khẩu");

		String htmlContent = "<!DOCTYPE html>"
				+ "<html>"
				+ "<head>"
				+ "<style>"
				+ "  .button {"
				+ "    background-color: #007BFF; color: white !important; padding: 12px 25px; text-align: center;"
				+ "    text-decoration: none !important; display: inline-block; font-size: 16px; margin: 10px 2px;"
				+ "    cursor: pointer; border-radius: 8px; font-family: Arial, sans-serif;"
				+ "  }"
				+ "</style>"
				+ "</head>"
				+ "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 40px; margin: 0;\">"
				+ "  <div style=\"max-width: 600px; margin: auto; background-color: white; padding: 40px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); text-align: center;\">"
				+ "    <h2 style=\"color: #333;\">Yêu Cầu Reset Mật Khẩu</h2>"
				+ "    <p>Chúng tôi nhận được yêu cầu reset mật khẩu cho tài khoản của bạn.</p>"
				+ "    <p>Vui lòng nhấp vào nút bên dưới để đặt lại mật khẩu:</p>"
				+ "    <a href=\"" + resetLink
				+ "\" class=\"button\" style=\"text-decoration: none; color: white;\">Reset Mật Khẩu</a>"
				+ "    <p style=\"color: #888; font-size: 12px; margin-top: 20px;\">Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>"
				+ "  </div>"
				+ "</body>"
				+ "</html>";

		helper.setText(htmlContent, true);

		mailSender.send(message);
	}

	public String formatDate(LocalDate orderDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("vi-VN"));
		return orderDate.format(formatter);
	}

	public String formatPrice(BigDecimal price) {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		return numberFormat.format(price);
	}
}
