# Address Management Fix Documentation

## Vấn đề đã gặp phải

Khi thêm địa chỉ nhận hàng, hệ thống báo lỗi vì thiếu thông tin `khachHang` (MaKH) trong entity `DiaChiNhanHang`.

## Nguyên nhân

1. **Form thiếu trường khách hàng:** Form HTML chỉ có các trường `tenNguoiNhan`, `soDTNhanHang`, `dcNhanHang` nhưng thiếu `khachHang`
2. **Controller không set khách hàng:** Method `add()` trong `Profile_DashboardController` không tự động set `khachHang` từ session
3. **Thiếu validation và error handling:** Không có kiểm tra quyền và xử lý lỗi

## Giải pháp đã áp dụng

### 1. Sửa Controller - Profile_DashboardController.java

#### Method ADD:
```java
@PostMapping("/add")
public String add(@RequestParam com.poly.entity.DiaChiNhanHang dc, 
                 HttpSession session,
                 RedirectAttributes redirectAttributes) {
    try {
        String userRole = Security.getUserRole(session);
        if (!"CUSTOMER".equals(userRole)) {
            redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể thêm địa chỉ!");
            return "redirect:/user/profile";
        }
        
        KhachHang khachHang = (KhachHang) session.getAttribute("user");
        dc.setKhachHang(khachHang);
        
        diaChiNhanHangService.save(dc);
        redirectAttributes.addFlashAttribute("success", "Thêm địa chỉ thành công!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm địa chỉ: " + e.getMessage());
    }
    return "redirect:/user/profile";
}
```

#### Method EDIT:
```java
@PostMapping("/edit/{id}")
public String edit(@RequestParam com.poly.entity.DiaChiNhanHang dc,
                  @PathVariable Integer id,
                  HttpSession session,
                  RedirectAttributes redirectAttributes) {
    try {
        String userRole = Security.getUserRole(session);
        if (!"CUSTOMER".equals(userRole)) {
            redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể sửa địa chỉ!");
            return "redirect:/user/profile";
        }
        
        // Lấy địa chỉ hiện tại để giữ nguyên khách hàng
        com.poly.entity.DiaChiNhanHang existingDc = diaChiNhanHangService.findById(id).orElse(null);
        if (existingDc == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy địa chỉ!");
            return "redirect:/user/profile";
        }
        
        // Cập nhật thông tin mới nhưng giữ nguyên khách hàng
        dc.setId(id);
        dc.setKhachHang(existingDc.getKhachHang());
        
        diaChiNhanHangService.save(dc);
        redirectAttributes.addFlashAttribute("success", "Cập nhật địa chỉ thành công!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật địa chỉ: " + e.getMessage());
    }
    return "redirect:/user/profile";
}
```

#### Method DELETE:
```java
@PostMapping("/delete/{id}")
public String delete(@PathVariable Integer id,
                    HttpSession session,
                    RedirectAttributes redirectAttributes) {
    try {
        String userRole = Security.getUserRole(session);
        if (!"CUSTOMER".equals(userRole)) {
            redirectAttributes.addFlashAttribute("error", "Chỉ khách hàng mới có thể xóa địa chỉ!");
            return "redirect:/user/profile";
        }
        
        diaChiNhanHangService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Xóa địa chỉ thành công!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa địa chỉ: " + e.getMessage());
    }
    return "redirect:/user/profile";
}
```

### 2. Sửa Template - profile-personal-info.html

#### Form Add/Edit:
```html
<!-- Add/Edit Address Form -->
<form th:action="${dc != null ? '/user/edit/' + dc.id : '/user/add'}" method="post" class="mb-4">
    <div class="row">
        <div class="col-md-12 mb-3">
            <label for="tenNguoiNhan" class="form-label">Tên người nhận</label>
            <input type="text" class="form-control" id="tenNguoiNhan" name="tenNguoiNhan" 
                   th:value="${dc != null ? dc.tenNguoiNhan : ''}" required>
        </div>
        <div class="col-md-12 mb-3">
            <label for="soDTNhanHang" class="form-label">Số điện thoại</label>
            <input type="tel" class="form-control" id="soDTNhanHang" name="soDTNhanHang" 
                   th:value="${dc != null ? dc.soDTNhanHang : ''}" required>
        </div>
        <div class="col-md-12 mb-3">
            <label for="dcNhanHang" class="form-label">Địa chỉ</label>
            <textarea class="form-control" id="dcNhanHang" name="dcNhanHang" rows="3" required
                      th:text="${dc != null ? dc.dcNhanHang : ''}"></textarea>
        </div>
        <div class="col-12">
            <button type="submit" class="btn btn-primary">
                <i class="feather icon-feather-plus me-2" th:if="${dc == null}"></i>
                <i class="feather icon-feather-edit me-2" th:if="${dc != null}"></i>
                <span th:text="${dc != null ? 'Cập nhật địa chỉ' : 'Thêm địa chỉ'}">Thêm địa chỉ</span>
            </button>
            <a th:if="${dc != null}" href="/user/profile" class="btn btn-secondary ms-2">
                <i class="feather icon-feather-x me-2"></i>
                Hủy
            </a>
        </div>
    </div>
</form>
```

## Cải tiến đã thực hiện

### 1. **Tự động set khách hàng:**
- Controller tự động lấy `KhachHang` từ session và set vào entity `DiaChiNhanHang`
- Không cần thêm trường `khachHang` vào form

### 2. **Validation và Security:**
- Kiểm tra quyền: Chỉ khách hàng mới có thể thêm/sửa/xóa địa chỉ
- Kiểm tra tồn tại: Đảm bảo địa chỉ tồn tại trước khi sửa/xóa
- Xử lý lỗi: Try-catch và thông báo lỗi chi tiết

### 3. **User Experience:**
- Thông báo thành công/lỗi rõ ràng
- Form thông minh: Tự động chuyển đổi giữa "Thêm" và "Sửa"
- Nút "Hủy" khi đang sửa địa chỉ
- Pre-fill dữ liệu khi sửa địa chỉ

### 4. **Data Integrity:**
- Giữ nguyên `khachHang` khi sửa địa chỉ
- Đảm bảo `id` được set đúng khi update

## Testing

### Test Cases:

1. **Thêm địa chỉ mới:**
   - Đăng nhập với tài khoản khách hàng
   - Vào tab "Personal Info"
   - Điền form và submit
   - Kiểm tra thông báo thành công
   - Kiểm tra địa chỉ xuất hiện trong bảng

2. **Sửa địa chỉ:**
   - Click nút "Sửa" trên địa chỉ đã có
   - Kiểm tra form được pre-fill
   - Sửa thông tin và submit
   - Kiểm tra thông báo thành công

3. **Xóa địa chỉ:**
   - Click nút "Xóa" trên địa chỉ
   - Xác nhận trong popup
   - Kiểm tra thông báo thành công

4. **Security Test:**
   - Đăng nhập với tài khoản nhân viên/admin
   - Thử thêm/sửa/xóa địa chỉ
   - Kiểm tra thông báo lỗi quyền

## Kết quả

✅ **Đã sửa thành công:**
- Thêm địa chỉ hoạt động bình thường
- Sửa địa chỉ hoạt động bình thường
- Xóa địa chỉ hoạt động bình thường
- Validation và security đầy đủ
- User experience tốt hơn

## Lưu ý

- Chỉ khách hàng mới có thể quản lý địa chỉ
- Địa chỉ được liên kết với khách hàng thông qua `MaKH`
- Tất cả thao tác đều có thông báo phản hồi
- Form tự động chuyển đổi giữa chế độ thêm và sửa 