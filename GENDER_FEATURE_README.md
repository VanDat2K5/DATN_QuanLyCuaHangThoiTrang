# Tính năng Phân loại Sản phẩm theo Giới tính

## Tổng quan
Đã thêm tính năng phân loại sản phẩm theo giới tính vào hệ thống quản lý cửa hàng thời trang.

## Các thay đổi đã thực hiện

### 1. Entity SanPham
- Thêm thuộc tính `gioiTinh` kiểu `Gender` enum
- Tạo enum `Gender` với 4 giá trị: `NAM`, `NU`, `TRE_EM`, `UNISEX`
- Mặc định giá trị là `UNISEX`

### 2. Repository & Service
- Thêm các method tìm kiếm theo giới tính:
  - `findByGioiTinh(Gender gioiTinh)`
  - `findByLoaiSanPham_MaLoaiSPAndGioiTinh(String maLoaiSP, Gender gioiTinh)`
  - `findByTenSPContainingAndGioiTinh(String tenSP, Gender gioiTinh)`

### 3. Controller
- **ProductManagementController**: Hỗ trợ chọn giới tính khi tạo/sửa sản phẩm
- **PublicController**: Thêm lọc sản phẩm theo giới tính cho khách hàng

### 4. Database
- Thêm cột `GioiTinh` vào bảng `SanPham`
- Tạo index để tối ưu hiệu suất
- Thêm constraint để đảm bảo dữ liệu hợp lệ

## Cách sử dụng

### Cho Admin:
1. **Tạo sản phẩm mới**: Chọn giới tính trong form tạo sản phẩm
2. **Sửa sản phẩm**: Có thể thay đổi giới tính của sản phẩm
3. **Xem danh sách**: Hiển thị giới tính của từng sản phẩm

### Cho Khách hàng:
1. **Trang Shop**: Lọc sản phẩm theo giới tính
2. **Trang Collection**: Lọc sản phẩm theo giới tính
3. **URL Parameters**: 
   - `/shop?gender=NAM` - Xem sản phẩm nam
   - `/shop?gender=NU` - Xem sản phẩm nữ
   - `/shop?gender=TRE_EM` - Xem sản phẩm trẻ em
   - `/shop?gender=UNISEX` - Xem sản phẩm unisex

## Cập nhật Database

### Chạy script SQL:
```sql
-- Thêm cột giới tính
ALTER TABLE SanPham 
ADD GioiTinh NVARCHAR(20) NOT NULL DEFAULT 'UNISEX';

-- Tạo index
CREATE INDEX IX_SanPham_GioiTinh ON SanPham(GioiTinh);
CREATE INDEX IX_SanPham_LoaiSP_GioiTinh ON SanPham(MaLoaiSP, GioiTinh);

-- Thêm constraint
ALTER TABLE SanPham 
ADD CONSTRAINT CK_SanPham_GioiTinh 
CHECK (GioiTinh IN ('NAM', 'NU', 'TRE_EM', 'UNISEX'));
```

### Hoặc sử dụng file đã tạo:
```bash
# Chạy script database_update_gender.sql
```

## Lợi ích của việc thêm giới tính vào SanPham

### 1. Tính linh hoạt cao
- Một loại sản phẩm có thể có nhiều phiên bản cho các giới tính khác nhau
- Ví dụ: "Áo thun" có thể có "Áo thun nam", "Áo thun nữ", "Áo thun trẻ em"

### 2. Quản lý chi tiết
- Dễ dàng lọc và tìm kiếm sản phẩm theo giới tính
- Hỗ trợ marketing và bán hàng hiệu quả hơn

### 3. Trải nghiệm người dùng tốt hơn
- Khách hàng dễ dàng tìm sản phẩm phù hợp
- Giảm thời gian tìm kiếm

### 4. Báo cáo và thống kê
- Có thể phân tích doanh số theo giới tính
- Hỗ trợ quyết định kinh doanh

## So sánh với việc thêm vào LoaiSanPham

### Thêm vào SanPham (Đã chọn):
✅ **Ưu điểm:**
- Linh hoạt cao, một loại sản phẩm có thể có nhiều giới tính
- Dễ dàng quản lý và tìm kiếm
- Phù hợp với cấu trúc hiện tại

❌ **Nhược điểm:**
- Cần cập nhật dữ liệu hiện có
- Tăng kích thước bảng SanPham

### Thêm vào LoaiSanPham (Không chọn):
❌ **Nhược điểm:**
- Ít linh hoạt, phải tạo nhiều loại sản phẩm
- Khó quản lý khi có sản phẩm unisex
- Phải thay đổi logic hiện tại nhiều

## Kết luận
Việc thêm thuộc tính giới tính vào entity `SanPham` là lựa chọn tối ưu cho hệ thống của bạn, mang lại tính linh hoạt cao và dễ dàng mở rộng trong tương lai. 