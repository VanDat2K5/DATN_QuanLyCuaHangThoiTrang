# Tách form địa chỉ ra sidebar

## Mục tiêu
Tách form thêm/sửa địa chỉ ra khỏi tab và đặt nó ở cột bên hông (sidebar) để dễ sử dụng hơn.

## Thay đổi đã thực hiện

### 1. Tạo fragment mới cho form địa chỉ
**File**: `src/main/resources/templates/Client/fragments/profile-address-form.html`

- Tạo fragment riêng biệt cho form thêm/sửa địa chỉ
- Form có thể hoạt động ở cả chế độ thêm mới và chỉnh sửa
- Sử dụng `th:value` và `th:text` để pre-fill dữ liệu khi edit
- Có nút "Hủy" khi đang ở chế độ edit

### 2. Cập nhật fragment personal-info
**File**: `src/main/resources/templates/Client/fragments/profile-personal-info.html`

- Loại bỏ form địa chỉ khỏi tab personal-info
- Thay thế bằng include fragment form địa chỉ
- Giữ nguyên bảng hiển thị danh sách địa chỉ
- Layout 2 cột: thông tin cá nhân (trái) + form địa chỉ (phải)

## Cấu trúc mới

### Layout trong tab Personal Info:
```
┌─────────────────┬─────────────────┐
│   Thông tin     │   Form địa chỉ  │
│   cá nhân       │   (Sidebar)     │
│                 │                 │
└─────────────────┴─────────────────┘
│                                     │
│        Bảng địa chỉ đã lưu          │
│                                     │
└─────────────────────────────────────┘
```

### Fragment Structure:
- `profile-personal-info.html` - Tab chính chứa layout 2 cột
- `profile-address-form.html` - Fragment form địa chỉ (sidebar)
- Form được include vào cột bên phải của tab

## Lợi ích

### ✅ **UX tốt hơn:**
- Form địa chỉ luôn hiển thị ở sidebar, dễ tiếp cận
- Không cần scroll xuống để tìm form
- Layout rõ ràng, tách biệt chức năng

### ✅ **Dễ sử dụng:**
- Có thể thêm địa chỉ mới ngay lập tức
- Khi click "Sửa", form sẽ pre-fill dữ liệu
- Nút "Hủy" để quay về trạng thái ban đầu

### ✅ **Responsive:**
- Layout 2 cột trên desktop
- Tự động stack trên mobile
- Form vẫn hoạt động tốt trên mọi thiết bị

## Test cases

1. **Thêm địa chỉ mới**: 
   - Form trống ở sidebar → Nhập thông tin → Submit → Thành công

2. **Sửa địa chỉ**: 
   - Click "Sửa" → Form pre-fill → Chỉnh sửa → Submit → Thành công

3. **Hủy sửa**: 
   - Trong chế độ edit → Click "Hủy" → Quay về form trống

4. **Responsive**: 
   - Test trên mobile → Form stack xuống dưới

## Code thay đổi

### Fragment mới:
```html
<!-- profile-address-form.html -->
<div class="card" th:fragment="address-form">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="feather icon-feather-map-pin me-2"></i>
            <span th:text="${dc != null ? 'Sửa địa chỉ' : 'Thêm địa chỉ mới'}">Quản lý địa chỉ</span>
        </h5>
    </div>
    <div class="card-body">
        <form th:action="${dc != null ? '/user/edit/' + dc.id : '/user/add'}" method="post">
            <!-- Form fields -->
        </form>
    </div>
</div>
```

### Include trong personal-info:
```html
<!-- Address Form Sidebar -->
<div class="col-md-6">
    <div th:replace="Client/fragments/profile-address-form :: address-form"></div>
</div>
```

## Lưu ý
- Form action vẫn giữ nguyên logic `/user/edit/{id}` và `/user/add`
- Tất cả chức năng CRUD địa chỉ vẫn hoạt động bình thường
- Chỉ thay đổi vị trí hiển thị form để UX tốt hơn 