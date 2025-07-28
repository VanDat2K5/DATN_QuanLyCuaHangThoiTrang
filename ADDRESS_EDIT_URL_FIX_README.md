# Sửa lỗi URL mapping cho chức năng Edit địa chỉ

## Vấn đề
Khi click nút "Sửa" địa chỉ, trang load lại trang profile thay vì chuyển đến trang edit.

## Nguyên nhân
**URL Mapping Conflict**: Có 2 phương thức với cùng URL pattern `/user/edit/{id}`:
- `@GetMapping("/edit/{id}")` - để hiển thị form edit
- `@PostMapping("/edit/{id}")` - để xử lý form submit

Spring có thể bị nhầm lẫn khi xử lý các request đến URL này.

## Giải pháp đã áp dụng

### 1. Thay đổi URL cho phương thức GET
- **Trước**: `@GetMapping("/edit/{id}")`
- **Sau**: `@GetMapping("/edit-address/{id}")`

### 2. Cập nhật template
- **Trước**: `th:href="@{/user/edit/{id}(id=${diaChi.id})}"`
- **Sau**: `th:href="@{/user/edit-address/{id}(id=${diaChi.id})}"`

### 3. Thêm debug logging
Thêm các log để theo dõi luồng xử lý:
```java
System.out.println("[DEBUG] Edit GET called with id: " + id);
System.out.println("[DEBUG] Found address: " + dc.getTenNguoiNhan() + " - " + dc.getDcNhanHang());
System.out.println("[DEBUG] Loaded " + diaChiList.size() + " addresses for customer");
```

## Code thay đổi

### Controller
```java
// Trước
@GetMapping("/edit/{id}")
public String edit(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

// Sau  
@GetMapping("/edit-address/{id}")
public String editAddress(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
```

### Template
```html
<!-- Trước -->
<a th:href="@{/user/edit/{id}(id=${diaChi.id})}" class="btn btn-sm btn-warning" title="Sửa">

<!-- Sau -->
<a th:href="@{/user/edit-address/{id}(id=${diaChi.id})}" class="btn btn-sm btn-warning" title="Sửa">
```

## Kết quả mong đợi
- ✅ Click "Sửa" → Chuyển đến trang edit với form pre-fill
- ✅ Form hiển thị dữ liệu của địa chỉ cần sửa
- ✅ Có thể chỉnh sửa và submit
- ✅ Có thể click "Hủy" để quay về

## Test cases
1. **Click Sửa**: Click nút "Sửa" → URL thay đổi thành `/user/edit-address/{id}` → Form pre-fill dữ liệu
2. **Edit và Submit**: Chỉnh sửa → Submit → Thành công → Quay về profile
3. **Hủy**: Click "Hủy" → Quay về profile
4. **Debug logs**: Kiểm tra console để xem debug logs

## Lưu ý
- Form action vẫn giữ nguyên `/user/edit/{id}` (POST) vì không có conflict
- Chỉ thay đổi URL cho GET request để tránh conflict
- Debug logs sẽ giúp theo dõi luồng xử lý nếu có vấn đề 