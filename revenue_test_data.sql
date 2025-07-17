-- Dữ liệu mẫu để test báo cáo doanh thu
USE DATN;

-- Thêm thêm sản phẩm
INSERT INTO SanPham (MaSP, MaLoaiSP, TenSP, GioiTinh)
VALUES 
('SP003', 'LSP001', N'Áo thun nữ cổ tròn', 'NU'),
('SP004', 'LSP002', N'Quần jean nam', 'NAM'),
('SP005', 'LSP001', N'Áo thun unisex', 'UNISEX');

-- Thêm chi tiết sản phẩm
INSERT INTO ChiTietSanPham (ID_SPCT, MaSP, MaMau, MaSize, SoLuong, GiaNhap, GiaXuat, LoHang)
VALUES 
('CTSP003', 'SP003', 'M001', 'S', 40, 70000, 110000, N'LH003'),
('CTSP004', 'SP004', 'M002', 'M', 35, 120000, 180000, N'LH004'),
('CTSP005', 'SP005', 'M001', 'M', 60, 75000, 115000, N'LH005');

-- Thêm phiếu nhập hàng
INSERT INTO PhieuNhap (MaPN, MaNV, TenNhaCungCap, NgayNhap)
VALUES 
('PN002', 'NV001', N'Công ty XYZ', '2025-01-15'),
('PN003', 'NV001', N'Công ty DEF', '2025-01-20'),
('PN004', 'NV001', N'Công ty GHI', '2025-02-01'),
('PN005', 'NV001', N'Công ty JKL', '2025-02-10');

-- Thêm chi tiết phiếu nhập
INSERT INTO ChiTietPhieuNhap (ID_SPCT, MaPN, GiaNhap, SoLuong, LoHang)
VALUES 
('CTSP002', 'PN002', 100000, 30, N'LH002'),
('CTSP003', 'PN003', 70000, 40, N'LH003'),
('CTSP004', 'PN004', 120000, 35, N'LH004'),
('CTSP005', 'PN005', 75000, 60, N'LH005');

-- Thêm hóa đơn bán hàng
INSERT INTO HoaDon (MaHD, MaNV, MaKH, MaKM, TongTien, NgayLap, TrangThai, PT_ThanhToan, SoDT_NhanHang, DC_NhanHang, Ten_NguoiNhan)
VALUES 
('HD004', 'NV001', 'KH001', 'KM001', 360000, '2025-01-15', N'DaThanhToan', N'Bank', '0901234567', N'123 Lê Lợi', N'Nguyễn Văn A'),
('HD005', 'NV001', 'KH002', NULL, 330000, '2025-01-20', N'DaThanhToan', N'COD', '0912345678', N'456 Nguyễn Huệ', N'Lê Thị B'),
('HD006', 'NV001', 'KH003', 'KM001', 690000, '2025-02-01', N'DaThanhToan', N'Bank', '0855022430', N'369,Tô Ký', N'Nguyễn Văn Đạt'),
('HD007', 'NV001', 'KH001', NULL, 345000, '2025-02-10', N'DaThanhToan', N'COD', '0901234567', N'123 Lê Lợi', N'Nguyễn Văn A'),
('HD008', 'NV001', 'KH002', 'KM001', 180000, '2025-02-15', N'DaThanhToan', N'Bank', '0912345678', N'456 Nguyễn Huệ', N'Lê Thị B'),
('HD009', 'NV001', 'KH003', NULL, 230000, '2025-02-20', N'DaThanhToan', N'COD', '0855022430', N'369,Tô Ký', N'Nguyễn Văn Đạt');

-- Thêm chi tiết hóa đơn
INSERT INTO ChiTietHoaDon (ID_SPCT, MaHD, GiaXuat, SoLuongXuat, LoHang, ThanhTien)
VALUES 
-- HD004: 3 sản phẩm SP002
('CTSP002', 'HD004', 150000, 2, N'LH002', 300000),
('CTSP002', 'HD004', 150000, 1, N'LH002', 150000),

-- HD005: 2 sản phẩm SP003, 1 sản phẩm SP001
('CTSP003', 'HD005', 110000, 2, N'LH003', 220000),
('CTSP001', 'HD005', 120000, 1, N'LH001', 120000),

-- HD006: 2 sản phẩm SP004, 3 sản phẩm SP005
('CTSP004', 'HD006', 180000, 2, N'LH004', 360000),
('CTSP005', 'HD006', 115000, 3, N'LH005', 345000),

-- HD007: 1 sản phẩm SP003, 2 sản phẩm SP005
('CTSP003', 'HD007', 110000, 1, N'LH003', 110000),
('CTSP005', 'HD007', 115000, 2, N'LH005', 230000),

-- HD008: 1 sản phẩm SP004
('CTSP004', 'HD008', 180000, 1, N'LH004', 180000),

-- HD009: 2 sản phẩm SP001
('CTSP001', 'HD009', 120000, 2, N'LH001', 240000);

-- Cập nhật tổng tiền cho các hóa đơn
UPDATE HoaDon SET TongTien = 450000 WHERE MaHD = 'HD004';
UPDATE HoaDon SET TongTien = 330000 WHERE MaHD = 'HD005';
UPDATE HoaDon SET TongTien = 705000 WHERE MaHD = 'HD006';
UPDATE HoaDon SET TongTien = 340000 WHERE MaHD = 'HD007';
UPDATE HoaDon SET TongTien = 180000 WHERE MaHD = 'HD008';
UPDATE HoaDon SET TongTien = 240000 WHERE MaHD = 'HD009'; 