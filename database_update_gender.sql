-- Script để thêm cột giới tính vào bảng SanPham
-- Chạy script này để cập nhật database schema

-- Thêm cột GioiTinh vào bảng SanPham
ALTER TABLE SanPham 
ADD GioiTinh NVARCHAR(20) NOT NULL DEFAULT 'UNISEX';

-- Thêm cột tongTien vào bảng PhieuNhap
ALTER TABLE PhieuNhap 
ADD tongTien DECIMAL(18,2) NULL;

-- Cập nhật dữ liệu mẫu cho các sản phẩm hiện có (tùy chọn)
-- Bạn có thể cập nhật theo logic nghiệp vụ của mình

-- Ví dụ: Cập nhật một số sản phẩm mẫu
-- UPDATE SanPham SET GioiTinh = 'NAM' WHERE TenSP LIKE '%nam%' OR TenSP LIKE '%Nam%';
-- UPDATE SanPham SET GioiTinh = 'NU' WHERE TenSP LIKE '%nữ%' OR TenSP LIKE '%Nu%' OR TenSP LIKE '%Nữ%';
-- UPDATE SanPham SET GioiTinh = 'TRE_EM' WHERE TenSP LIKE '%trẻ%' OR TenSP LIKE '%tre%' OR TenSP LIKE '%Trẻ%';

-- Tạo index để tối ưu hiệu suất tìm kiếm theo giới tính
CREATE INDEX IX_SanPham_GioiTinh ON SanPham(GioiTinh);

-- Tạo index kết hợp cho tìm kiếm theo loại sản phẩm và giới tính
CREATE INDEX IX_SanPham_LoaiSP_GioiTinh ON SanPham(MaLoaiSP, GioiTinh);

-- Thêm constraint để đảm bảo giá trị giới tính hợp lệ
ALTER TABLE SanPham 
ADD CONSTRAINT CK_SanPham_GioiTinh 
CHECK (GioiTinh IN ('NAM', 'NU', 'TRE_EM', 'UNISEX'));

-- Commit thay đổi
COMMIT;

-- Kiểm tra kết quả
SELECT MaSP, TenSP, GioiTinh FROM SanPham LIMIT 10; 