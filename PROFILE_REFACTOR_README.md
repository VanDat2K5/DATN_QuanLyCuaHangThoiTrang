# Profile Refactor - Tách thành các Fragment

## Tổng quan
Đã refactor trang Profile thành các fragment riêng biệt để dễ dàng tách controller và quản lý code.

## Cấu trúc mới

### 1. File chính
- `demo-fashion-store-profile-new.html` - File profile chính sử dụng các fragment

### 2. Các Fragment (trong thư mục `Client/fragments/`)

#### `profile-dashboard.html`
- **Chức năng**: Hiển thị thống kê và danh sách đơn hàng
- **Fragment name**: `dashboard`
- **Controller tương lai**: `DashboardController`

#### `profile-personal-info.html`
- **Chức năng**: Thông tin cá nhân và quản lý địa chỉ nhận hàng
- **Fragment name**: `info`
- **Controller tương lai**: `PersonalInfoController`

#### `profile-edit.html`
- **Chức năng**: Form chỉnh sửa thông tin cá nhân
- **Fragment name**: `edit`
- **Controller tương lai**: `ProfileEditController`

#### `profile-change-password.html`
- **Chức năng**: Form đổi mật khẩu
- **Fragment name**: `password`
- **Controller tương lai**: `PasswordController`

#### `profile-create-account.html`
- **Chức năng**: Form tạo tài khoản cho OAuth users
- **Fragment name**: `account`
- **Controller tương lai**: `AccountController`

## Cách sử dụng

### 1. Sử dụng file mới
```html
<!-- Thay thế file cũ bằng file mới -->
<div th:replace="Client/demo-fashion-store-profile-new"></div>
```

### 2. Sử dụng từng fragment riêng lẻ
```html
<!-- Sử dụng fragment dashboard -->
<div th:replace="Client/fragments/profile-dashboard :: dashboard"></div>

<!-- Sử dụng fragment personal info -->
<div th:replace="Client/fragments/profile-personal-info :: info"></div>

<!-- Sử dụng fragment edit profile -->
<div th:replace="Client/fragments/profile-edit :: edit"></div>

<!-- Sử dụng fragment change password -->
<div th:replace="Client/fragments/profile-change-password :: password"></div>

<!-- Sử dụng fragment create account -->
<div th:replace="Client/fragments/profile-create-account :: account"></div>
```

## Lợi ích của việc tách fragment

### 1. **Dễ bảo trì**
- Mỗi chức năng được tách riêng
- Dễ dàng sửa đổi từng phần mà không ảnh hưởng phần khác
- Code ngắn gọn và dễ đọc hơn

### 2. **Dễ tách controller**
- Mỗi fragment tương ứng với một controller riêng
- Có thể tách `AuthController` thành nhiều controller nhỏ:
  - `DashboardController` - Xử lý thống kê và đơn hàng
  - `PersonalInfoController` - Xử lý thông tin cá nhân và địa chỉ
  - `ProfileEditController` - Xử lý chỉnh sửa profile
  - `PasswordController` - Xử lý đổi mật khẩu
  - `AccountController` - Xử lý tạo tài khoản OAuth

### 3. **Tái sử dụng**
- Có thể sử dụng các fragment ở nhiều nơi khác nhau
- Ví dụ: Fragment dashboard có thể dùng cho admin dashboard

### 4. **Phát triển song song**
- Nhiều developer có thể làm việc trên các fragment khác nhau
- Giảm conflict khi merge code

## Kế hoạch tách controller

### Bước 1: Tạo các controller mới
```java
@Controller
@RequestMapping("/user/dashboard")
public class DashboardController {
    // Xử lý thống kê và đơn hàng
}

@Controller
@RequestMapping("/user/profile")
public class PersonalInfoController {
    // Xử lý thông tin cá nhân và địa chỉ
}

@Controller
@RequestMapping("/user/edit")
public class ProfileEditController {
    // Xử lý chỉnh sửa profile
}

@Controller
@RequestMapping("/user/password")
public class PasswordController {
    // Xử lý đổi mật khẩu
}

@Controller
@RequestMapping("/user/account")
public class AccountController {
    // Xử lý tạo tài khoản OAuth
}
```

### Bước 2: Cập nhật URL mapping
```java
// Thay vì tất cả trong AuthController
@GetMapping("/profile") // Cũ
@GetMapping("/dashboard") // Mới - DashboardController
@GetMapping("/profile/info") // Mới - PersonalInfoController
@GetMapping("/profile/edit") // Mới - ProfileEditController
@GetMapping("/profile/password") // Mới - PasswordController
@GetMapping("/profile/account") // Mới - AccountController
```

### Bước 3: Tách service layer
```java
// Tạo các service riêng cho từng chức năng
public interface DashboardService {
    Page<HoaDon> getOrders(String userId, Pageable pageable);
    Map<String, Object> getStatistics(String userId);
}

public interface PersonalInfoService {
    List<DiaChiNhanHang> getAddresses(String userId);
    void addAddress(DiaChiNhanHang address);
    void updateAddress(DiaChiNhanHang address);
    void deleteAddress(Integer addressId);
}
```

## Cách test

### 1. Test file mới
```bash
# Truy cập profile với file mới
http://localhost:8080/user/profile
```

### 2. Test từng fragment
```html
<!-- Test fragment dashboard -->
<div th:replace="Client/fragments/profile-dashboard :: dashboard"></div>

<!-- Test fragment personal info -->
<div th:replace="Client/fragments/profile-personal-info :: info"></div>
```

## Lưu ý

### 1. **Dependencies**
- Các fragment cần các biến model giống nhau
- Đảm bảo controller truyền đầy đủ data cho tất cả fragment

### 2. **JavaScript**
- JavaScript được đặt trong file chính
- Có thể tách thành file riêng cho từng fragment nếu cần

### 3. **CSS**
- CSS được đặt trong file chính
- Có thể tách thành file riêng cho từng fragment nếu cần

### 4. **Backward Compatibility**
- File cũ vẫn hoạt động bình thường
- Có thể chuyển đổi dần dần

## Kết luận

Việc tách profile thành các fragment giúp:
- **Code sạch hơn** và dễ bảo trì
- **Dễ dàng tách controller** trong tương lai
- **Tái sử dụng code** tốt hơn
- **Phát triển song song** hiệu quả hơn

Đây là bước đầu tiên để cải thiện kiến trúc của hệ thống, chuẩn bị cho việc tách controller và service layer. 