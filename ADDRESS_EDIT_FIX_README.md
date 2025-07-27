# Sửa lỗi chức năng Edit địa chỉ

## Vấn đề
Chức năng thêm và xóa địa chỉ hoạt động bình thường, nhưng chức năng sửa địa chỉ bị lỗi.

## Nguyên nhân
1. **Thiếu dữ liệu trong Model**: Phương thức `edit` (GET) chỉ load đối tượng `DiaChiNhanHang` cần sửa mà không load các dữ liệu khác cần thiết cho template.
2. **Template cần dữ liệu đầy đủ**: Template `demo-fashion-store-profile-new` cần tất cả các thuộc tính như `user`, `userRole`, `diaChiList`, etc.

## Giải pháp đã áp dụng

### 1. Sửa phương thức `edit` (GET) trong `Profile_DashboardController.java`
- **Thêm dữ liệu user**: Load và thêm `user`, `userRole`, `username` vào model
- **Thêm dữ liệu OAuth**: Load và thêm `oauthUser`, `needsAccount` vào model  
- **Load danh sách địa chỉ**: Load tất cả địa chỉ của khách hàng để hiển thị trong bảng
- **Giữ nguyên đối tượng cần sửa**: Vẫn load đối tượng `dc` để pre-fill form

### 2. Đảm bảo tính nhất quán
- Sử dụng `@ModelAttribute` cho cả phương thức `add` và `edit` (POST)
- Sử dụng `com.poly.entity.DiaChiNhanHang` đầy đủ thay vì `DiaChiNhanHang` ngắn gọn

## Code thay đổi

### Trước:
```java
@GetMapping("/edit/{id}")
public String edit(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    // Chỉ load đối tượng dc
    com.poly.entity.DiaChiNhanHang dc = diaChiNhanHangService.findById(id).orElse(null);
    model.addAttribute("dc", dc);
    return "Client/demo-fashion-store-profile-new";
}
```

### Sau:
```java
@GetMapping("/edit/{id}")
public String edit(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    // Load tất cả dữ liệu cần thiết
    Object user = session.getAttribute("user");
    model.addAttribute("user", user);
    model.addAttribute("userRole", userRole);
    model.addAttribute("username", session.getAttribute("username"));
    
    // Load danh sách địa chỉ
    List<com.poly.entity.DiaChiNhanHang> diaChiList = diaChiNhanHangService
            .findByKhachHang_MaKH(khachHang.getMaKH());
    model.addAttribute("diaChiList", diaChiList);
    
    // Load đối tượng cần sửa
    model.addAttribute("dc", dc);
    return "Client/demo-fashion-store-profile-new";
}
```

## Kết quả
- ✅ Thêm địa chỉ: Hoạt động bình thường
- ✅ Xóa địa chỉ: Hoạt động bình thường  
- ✅ Sửa địa chỉ: Đã được sửa và hoạt động bình thường

## Test cases
1. **Thêm địa chỉ mới**: Form trống → Nhập thông tin → Submit → Thành công
2. **Sửa địa chỉ**: Click "Sửa" → Form pre-fill → Chỉnh sửa → Submit → Thành công
3. **Xóa địa chỉ**: Click "Xóa" → Confirm → Thành công
4. **Hủy sửa**: Trong chế độ edit → Click "Hủy" → Quay về trang profile

## Lưu ý
- Template đã được thiết kế để hỗ trợ cả add và edit trong cùng một form
- Sử dụng `th:action` động để chuyển hướng đến đúng endpoint
- Sử dụng `th:value` và `th:text` để pre-fill dữ liệu khi edit 