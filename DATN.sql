CREATE DATABASE DATN;
GO

USE DATN;
GO

CREATE TABLE KhachHang (
    MaKH VARCHAR(20) PRIMARY KEY,
    TenKH NVARCHAR(255),
    Email NVARCHAR(50) UNIQUE NOT NULL,
    SoDT VARCHAR(10)
);

CREATE TABLE NhanVien (
    MaNV VARCHAR(20) PRIMARY KEY,
    TenNV NVARCHAR(255) NOT NULL,
    Email NVARCHAR(50) UNIQUE NOT NULL,
    SoDT VARCHAR(10) NOT NULL,
	isAdmin BIT DEFAULT 0,
	isActivity BIT DEFAULT 1
);

CREATE TABLE TaiKhoan (
    ID_INT INT IDENTITY(1,1) PRIMARY KEY,
    ID_MaNV VARCHAR(20),
    ID_MaKH VARCHAR(20),
    TenTK NVARCHAR(255) NOT NULL,
    MatKhau NVARCHAR(255) NOT NULL,
    HinhAnh VARCHAR(50),
    FOREIGN KEY (ID_MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (ID_MaKH) REFERENCES KhachHang(MaKH)
);

CREATE TABLE KhuyenMai (
    MaKM VARCHAR(20) PRIMARY KEY,
    TenKM NVARCHAR(255) NOT NULL,
    Giam INT NOT NULL,
    GioiHan INT NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL
);

CREATE TABLE LoaiSanPham (
    MaLoaiSP VARCHAR(20) PRIMARY KEY,
    LoaiSP NVARCHAR(150) NOT NULL
);

CREATE TABLE SanPham (
    MaSP VARCHAR(20) PRIMARY KEY,
    MaLoaiSP VARCHAR(20) NOT NULL,
    TenSP NVARCHAR(255) NOT NULL,
	GioiTinh NVARCHAR(20) CHECK (GioiTinh IN ('NAM', 'NU', 'TRE_EM', 'UNISEX')) DEFAULT 'UNISEX',
    FOREIGN KEY (MaLoaiSP) REFERENCES LoaiSanPham(MaLoaiSP)
);

CREATE TABLE Mau (
    MaMau VARCHAR(20) PRIMARY KEY,
    TenMau NVARCHAR(255) NOT NULL
);

CREATE TABLE Size (
    MaSize VARCHAR(20) PRIMARY KEY,
    TenSize NVARCHAR(50) NOT NULL
);

CREATE TABLE ChiTietSanPham (
    ID_SPCT VARCHAR(50) PRIMARY KEY,
    MaSP VARCHAR(20) NOT NULL,
    MaMau VARCHAR(20) NOT NULL,
    MaSize VARCHAR(20) NOT NULL,
    SoLuong INT NOT NULL,
    GiaNhap DECIMAL(18,2) NOT NULL,
    GiaXuat DECIMAL(18,2) NOT NULL,
    LoHang NVARCHAR(50) NOT NULL,
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP),
    FOREIGN KEY (MaMau) REFERENCES Mau(MaMau),
    FOREIGN KEY (MaSize) REFERENCES Size(MaSize)
);

CREATE TABLE HinhAnh (
    ID_Anh INT IDENTITY(1,1) PRIMARY KEY,
    MaSP VARCHAR(20) NOT NULL,
    HinhAnh NVARCHAR(255) NOT NULL,
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);


CREATE TABLE HoaDon (
    MaHD VARCHAR(20) PRIMARY KEY,
    MaNV VARCHAR(20),
    MaKH VARCHAR(20) NOT NULL,
    MaKM VARCHAR(20),
    TongTien DECIMAL(18,2),
    NgayLap DATE NOT NULL,
    TrangThai NVARCHAR(255) CHECK (TrangThai IN ('DaThanhToan', 'DangVanChuyen', 'DaNhanHang', 'DaHuy', 'Doi-Tra_Hang', 'ChoXacNhan')) DEFAULT 'ChoXacNhan',
    PT_ThanhToan NVARCHAR(255) CHECK (PT_ThanhToan IN ('COD', 'Bank')) NOT NULL,
    SoDT_NhanHang NVARCHAR(10) NOT NULL,
    DC_NhanHang NVARCHAR(255) NOT NULL,
    Ten_NguoiNhan NVARCHAR(255) NOT NULL,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM)
);

CREATE TABLE ChiTietHoaDon (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    ID_SPCT VARCHAR(50) NOT NULL,
    MaHD VARCHAR(20) NOT NULL,
    GiaXuat DECIMAL(18,2) NOT NULL,
    SoLuongXuat INT NOT NULL,
    LoHang NVARCHAR(50) NOT NULL,
	ThanhTien DECIMAL(18,2) NOT NULL,
    FOREIGN KEY (ID_SPCT) REFERENCES ChiTietSanPham(ID_SPCT),
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD)
);

CREATE TABLE PhieuNhap (
    MaPN VARCHAR(20) PRIMARY KEY,
    MaNV VARCHAR(20) NOT NULL,
    TenNhaCungCap NVARCHAR(255) NOT NULL,
    NgayNhap DATE NOT NULL,
	tongTien DECIMAL(18,2) NULL,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE ChiTietPhieuNhap (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    ID_SPCT VARCHAR(50) NOT NULL,
    MaPN VARCHAR(20) NOT NULL,
    GiaNhap DECIMAL(18,2) NOT NULL,
    SoLuong INT NOT NULL,
    LoHang NVARCHAR(50) NOT NULL,
    FOREIGN KEY (ID_SPCT) REFERENCES ChiTietSanPham(ID_SPCT),
    FOREIGN KEY (MaPN) REFERENCES PhieuNhap(MaPN)
);

CREATE TABLE DiaChiNhanHang (
    ID INT IDENTITY(1,1) PRIMARY KEY,
	MaKH VARCHAR(20) NOT NULL,
    Ten_NguoiNhan NVARCHAR(255) NOT NULL,
    SoDT_NhanHang NVARCHAR(10) NOT NULL,
    DC_NhanHang NVARCHAR(255) NOT NULL,
    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH)
);

-- KhachHang
INSERT INTO KhachHang (MaKH, TenKH, Email, SoDT)
VALUES 
('KH001', N'Nguyễn Văn Đạt', 'vandat05022005@gmail.com', '0855022430'),
('KH002', N'Phạm Thành Hưng', 'thhung07@gmail.com', '0334043217');

-- NhanVien
INSERT INTO NhanVien (MaNV, TenNV, Email, SoDT)
VALUES 
('NV001', N'Nguyễn Anh Đô', 'anhdo28@gmail.com', '0923456789'),
('NV002', N'Cao T.T Hiền', 'thaohien@gmail.com', '0943534532');

-- TaiKhoan
INSERT INTO TaiKhoan (ID_MaNV, ID_MaKH, TenTK, MatKhau, HinhAnh)
VALUES 
('NV001', NULL, 'admin1', '123456', 'admin.jpg'),
(NULL, 'KH001', 'khach1', 'abc123', 'user1.jpg'),
('NV002', NULL, 'admin', '1111', 'admin.jpg'),
(NULL, 'KH002', 'thhung', 'hung123', 'user2.jpg');


-- KhuyenMai
INSERT INTO KhuyenMai (MaKM, TenKM, Giam, GioiHan, NgayBatDau, NgayKetThuc)
VALUES ('KM001', N'Giảm giá hè', 10, 100, '2025-06-01', '2025-06-30');

-- LoaiSanPham
INSERT INTO LoaiSanPham (MaLoaiSP, LoaiSP) VALUES
('Balo', N'Balo'),
('Jacket', N'Jacket'),
('Polo Shirt', N'Polo Shirt'),
('Sneaker', N'Sneaker'),
('Shorts', N'Shorts'),
('T-Shirt', N'T-Shirt');


INSERT INTO SanPham (MaSP, MaLoaiSP, TenSP, GioiTinh) VALUES
('SP001', 'T-Shirt', N'Adidas T-Shirt 1', 'NAM'),
('SP002', 'T-Shirt', N'Adidas T-Shirt 2', 'UNISEX'),
('SP003', 'T-Shirt', N'Adidas T-Shirt 3', 'NU'),
('SP004', 'T-Shirt', N'Nike T-Shirt 1', 'UNISEX'),
('SP005', 'Polo Shirt', N'Nike Polo 1', 'NAM'),
('SP006', 'Polo Shirt', N'Nike Polo 2', 'NAM'),
('SP007', 'Polo Shirt', N'Adidas Polo 1', 'NAM'),
('SP008', 'Polo Shirt', N'Adidas Polo 2', 'NU'),
('SP009', 'Polo Shirt', N'Adidas Polo 3', 'NAM'),
('SP010', 'Shorts', N'Adidas Short 1', 'UNISEX'),
('SP011', 'Sneaker', N'Giày Nike Air Force 1', 'UNISEX'),
('SP012', 'Balo', N'Nike Balo 1', 'UNISEX'),
('SP013', 'Jacket', N'Nike Jacket 1', 'NAM'),
('SP014', 'Jacket', N'Adidas Jacket 1', 'NU');


-- Mau
INSERT INTO Mau (MaMau, TenMau)
VALUES ('BLACK', N'Black'), ('WHITE', N'White'), ('BLUE', N'Blue'), ('PINK', N'Pink'), ('GRAY', N'Gray'), ('RED', N'Red');

-- Size
INSERT INTO Size (MaSize, TenSize)
VALUES ('S', 'Small'), ('M', 'Medium'), ('L', 'Large'), ('XL', 'Extra Large'), ('38', '38'), ('39', '39');

-- ChiTietSanPham
INSERT INTO ChiTietSanPham (ID_SPCT, MaSP, MaMau, MaSize, SoLuong, GiaNhap, GiaXuat, LoHang) VALUES
('SP001_BLACK_S', 'SP001', 'BLACK', 'S', 20, 100000, 150000, N'LH001'),
('SP001_BLACK_M', 'SP001', 'BLACK', 'M', 20, 100000, 150000, N'LH001'),
('SP001_BLACK_L', 'SP001', 'BLACK', 'L', 20, 100000, 150000, N'LH001'),
('SP001_BLACK_XL', 'SP001', 'BLACK', 'XL', 20, 100000, 150000, N'LH001'),
('SP001_WHITE_S', 'SP001', 'WHITE', 'S', 20, 100000, 150000, N'LH001'),
('SP001_WHITE_M', 'SP001', 'WHITE', 'M', 20, 100000, 150000, N'LH001'),
('SP001_WHITE_L', 'SP001', 'WHITE', 'L', 20, 100000, 150000, N'LH001'),
('SP001_WHITE_XL', 'SP001', 'WHITE', 'XL', 20, 100000, 150000, N'LH001'),
('SP001_BLUE_S', 'SP001', 'BLUE', 'S', 20, 100000, 150000, N'LH001'),
('SP001_BLUE_M', 'SP001', 'BLUE', 'M', 20, 100000, 150000, N'LH001'),
('SP001_BLUE_L', 'SP001', 'BLUE', 'L', 20, 100000, 150000, N'LH001'),
('SP001_BLUE_XL', 'SP001', 'BLUE', 'XL', 20, 100000, 150000, N'LH001'),
('SP002_BLACK_S', 'SP002', 'BLACK', 'S', 20, 100000, 150000, N'LH001'),
('SP002_BLACK_M', 'SP002', 'BLACK', 'M', 20, 100000, 150000, N'LH001'),
('SP002_BLACK_L', 'SP002', 'BLACK', 'L', 20, 100000, 150000, N'LH001'),
('SP002_BLACK_XL', 'SP002', 'BLACK', 'XL', 20, 100000, 150000, N'LH001'),
('SP002_WHITE_S', 'SP002', 'WHITE', 'S', 20, 100000, 150000, N'LH001'),
('SP002_WHITE_M', 'SP002', 'WHITE', 'M', 20, 100000, 150000, N'LH001'),
('SP002_WHITE_L', 'SP002', 'WHITE', 'L', 20, 100000, 150000, N'LH001'),
('SP002_WHITE_XL', 'SP002', 'WHITE', 'XL', 20, 100000, 150000, N'LH001'),
('SP002_BLUE_S', 'SP002', 'BLUE', 'S', 20, 100000, 150000, N'LH001'),
('SP002_BLUE_M', 'SP002', 'BLUE', 'M', 20, 100000, 150000, N'LH001'),
('SP002_BLUE_L', 'SP002', 'BLUE', 'L', 20, 100000, 150000, N'LH001'),
('SP002_BLUE_XL', 'SP002', 'BLUE', 'XL', 20, 100000, 150000, N'LH001'),
('SP003_BLACK_S', 'SP003', 'BLACK', 'S', 20, 100000, 150000, N'LH001'),
('SP003_BLACK_M', 'SP003', 'BLACK', 'M', 20, 100000, 150000, N'LH001'),
('SP003_BLACK_L', 'SP003', 'BLACK', 'L', 20, 100000, 150000, N'LH001'),
('SP003_BLACK_XL', 'SP003', 'BLACK', 'XL', 20, 100000, 150000, N'LH001'),
('SP003_WHITE_S', 'SP003', 'WHITE', 'S', 20, 100000, 150000, N'LH001'),
('SP003_WHITE_M', 'SP003', 'WHITE', 'M', 20, 100000, 150000, N'LH001'),
('SP003_WHITE_L', 'SP003', 'WHITE', 'L', 20, 100000, 150000, N'LH001'),
('SP003_WHITE_XL', 'SP003', 'WHITE', 'XL', 20, 100000, 150000, N'LH001'),
('SP003_BLUE_S', 'SP003', 'BLUE', 'S', 20, 100000, 150000, N'LH001'),
('SP003_BLUE_M', 'SP003', 'BLUE', 'M', 20, 100000, 150000, N'LH001'),
('SP003_BLUE_L', 'SP003', 'BLUE', 'L', 20, 100000, 150000, N'LH001'),
('SP003_BLUE_XL', 'SP003', 'BLUE', 'XL', 20, 100000, 150000, N'LH001'),
('SP004_BLACK_S', 'SP004', 'BLACK', 'S', 20, 100000, 150000, N'LH001'),
('SP004_BLACK_M', 'SP004', 'BLACK', 'M', 20, 100000, 150000, N'LH001'),
('SP004_BLACK_L', 'SP004', 'BLACK', 'L', 20, 100000, 150000, N'LH001'),
('SP004_BLACK_XL', 'SP004', 'BLACK', 'XL', 20, 100000, 150000, N'LH001'),
('SP004_WHITE_S', 'SP004', 'WHITE', 'S', 20, 100000, 150000, N'LH001'),
('SP004_WHITE_M', 'SP004', 'WHITE', 'M', 20, 100000, 150000, N'LH001'),
('SP004_WHITE_L', 'SP004', 'WHITE', 'L', 20, 100000, 150000, N'LH001'),
('SP004_WHITE_XL', 'SP004', 'WHITE', 'XL', 20, 100000, 150000, N'LH001'),
('SP004_BLUE_S', 'SP004', 'BLUE', 'S', 20, 100000, 150000, N'LH001'),
('SP004_BLUE_M', 'SP004', 'BLUE', 'M', 20, 100000, 150000, N'LH001'),
('SP004_BLUE_L', 'SP004', 'BLUE', 'L', 20, 100000, 150000, N'LH001'),
('SP004_BLUE_XL', 'SP004', 'BLUE', 'XL', 20, 100000, 150000, N'LH001'),
('SP005_BLACK_S', 'SP005', 'BLACK', 'S', 20, 100000, 150000, N'LH001'),
('SP005_BLACK_M', 'SP005', 'BLACK', 'M', 20, 100000, 150000, N'LH001'),
('SP005_BLACK_L', 'SP005', 'BLACK', 'L', 20, 100000, 150000, N'LH001'),
('SP005_BLACK_XL', 'SP005', 'BLACK', 'XL', 20, 100000, 150000, N'LH001'),
('SP005_WHITE_S', 'SP005', 'WHITE', 'S', 20, 100000, 150000, N'LH001'),
('SP005_WHITE_M', 'SP005', 'WHITE', 'M', 20, 100000, 150000, N'LH001'),
('SP005_WHITE_L', 'SP005', 'WHITE', 'L', 20, 100000, 150000, N'LH001'),
('SP005_WHITE_XL', 'SP005', 'WHITE', 'XL', 20, 100000, 150000, N'LH001'),
('SP005_BLUE_S', 'SP005', 'BLUE', 'S', 20, 100000, 150000, N'LH001'),
('SP005_BLUE_M', 'SP005', 'BLUE', 'M', 20, 100000, 150000, N'LH001'),
('SP005_BLUE_L', 'SP005', 'BLUE', 'L', 20, 100000, 150000, N'LH001'),
('SP005_BLUE_XL', 'SP005', 'BLUE', 'XL', 20, 100000, 150000, N'LH001'),
('SP006_BLACK_S', 'SP006', 'BLACK', 'S', 20, 100000, 150000, N'LH001'),
('SP006_BLACK_M', 'SP006', 'BLACK', 'M', 20, 100000, 150000, N'LH001'),
('SP007_BLACK_L', 'SP007', 'BLACK', 'L', 20, 100000, 150000, N'LH001'),
('SP007_BLACK_XL', 'SP007', 'BLACK', 'XL', 20, 100000, 150000, N'LH001'),
('SP008_WHITE_S', 'SP008', 'WHITE', 'S', 20, 100000, 150000, N'LH001'),
('SP008_WHITE_M', 'SP008', 'WHITE', 'M', 20, 100000, 150000, N'LH001'),
('SP009_WHITE_L', 'SP009', 'WHITE', 'L', 20, 100000, 150000, N'LH001'),
('SP009_WHITE_XL', 'SP009', 'WHITE', 'XL', 20, 100000, 150000, N'LH001'),
('SP010_BLUE_S', 'SP010', 'BLUE', 'S', 20, 100000, 150000, N'LH001'),
('SP010_BLUE_M', 'SP010', 'BLUE', 'M', 20, 100000, 150000, N'LH001'),
('SP011_BLUE_38', 'SP011', 'BLUE', '38', 20, 100000, 150000, N'LH001'),
('SP011_BLUE_39', 'SP011', 'BLUE', '39', 20, 100000, 150000, N'LH001'),
('SP012_BLACK_S', 'SP012', 'BLACK', 'S', 20, 100000, 150000, N'LH001'),
('SP012_BLACK_M', 'SP012', 'BLACK', 'M', 20, 100000, 150000, N'LH001'),
('SP012_BLACK_L', 'SP012', 'BLACK', 'L', 20, 100000, 150000, N'LH001'),
('SP013_BLACK_XL', 'SP013', 'BLACK', 'XL', 20, 100000, 150000, N'LH001'),
('SP013_WHITE_S', 'SP013', 'WHITE', 'S', 20, 100000, 150000, N'LH001'),
('SP014_WHITE_M', 'SP014', 'WHITE', 'M', 20, 100000, 150000, N'LH001'),
('SP014_WHITE_L', 'SP014', 'WHITE', 'L', 20, 100000, 150000, N'LH001');


-- HinhAnh
INSERT INTO HinhAnh (MaSP, HinhAnh) VALUES
('SP001', 'tee_adidas1.png'),('SP001', 'tee_adidas1_1.png'),('SP001', 'tee_adidas1_2.png'),('SP001', 'tee_adidas1_3.png'),
('SP002', 'tee_adidas2.png'),('SP002', 'tee_adidas2_1.png'),('SP002', 'tee_adidas2_2.png'),('SP002', 'tee_adidas2_3.png'),
('SP003', 'tee_adidas3.png'),('SP003', 'tee_adidas3_1.png'),('SP003', 'tee_adidas3_2.png'),('SP003', 'tee_adidas3_3.png'),
('SP004', 'tee_nike1.png'),('SP004', 'tee_nike1_1.png'),('SP004', 'tee_nike1_2.png'),('SP004', 'tee_nike1_3.png'),
('SP005', 'polo_nike1.png'),('SP005', 'polo_nike1_1.png'),('SP005', 'polo_nike1_2.png'),
('SP006', 'polo_nike2.png'),('SP006', 'polo_nike2_1.png'),
('SP007', 'polo_adidas1.png'),('SP007', 'polo_adidas1_1.png'),('SP007', 'polo_adidas1_2.png'),('SP007', 'polo_adidas1_3.png'),
('SP008', 'polo_adidas2.png'),('SP008', 'polo_adidas2_1.png'),('SP008', 'polo_adidas2_2.png'),
('SP009', 'polo_adidas3.png'),('SP009', 'polo_adidas3_1.png'),('SP009', 'polo_adidas3_2.png'),('SP009', 'polo_adidas3_3.png'),
('SP010', 'short_adidas1.png'),('SP010', 'short_adidas1_1.png'),('SP010', 'short_adidas1_2.png'),('SP010', 'short_adidas1_3.png'),
('SP011', 'giay_nike1.png'),('SP011', 'giay_nike1_1.png'),('SP011', 'giay_nike1_2.png'),('SP011', 'giay_nike1_3.png'),
('SP012', 'balo_nike1.png'),('SP012', 'balo_nike1_1.png'),('SP012', 'balo_nike1_2.png'),('SP012', 'balo_nike1_3.png'),('SP012', 'balo_nike1_4.png'),
('SP013', 'jacket_nike1.png'),('SP013', 'jacket_nike1_1.png'),
('SP014', 'jacket_adidas1.png'),('SP014', 'jacket_adidas1_1.png'),('SP014', 'jacket_adidas1_2.png'),('SP014', 'jacket_adidas1_3.png');

-- HoaDon
INSERT INTO HoaDon (MaHD, MaNV, MaKH, MaKM, TongTien, NgayLap, TrangThai, PT_ThanhToan, SoDT_NhanHang, DC_NhanHang, Ten_NguoiNhan) VALUES
('HD001', 'NV001', 'KH001', 'KM001', 240000, '2025-06-06', N'DaThanhToan', N'COD', '0901234567', N'123 Lê Lợi', N'Nguyễn Văn A'),
('HD002', 'NV001', 'KH002', 'KM001', 240000, '2025-06-06', N'DaThanhToan', N'Bank', '0855022430', N'369,Tô Ký', N'Nguyễn Văn Đạt'),
('HD003', 'NV001', 'KH002', null, 240000, '2025-06-06', N'ChoXacNhan', N'Bank', '0855022430', N'369,Tô Ký', N'Nguyễn Văn Đạt'),
('HD004', 'NV002', 'KH001', 'KM001', 950000, '2025-06-15', N'DangVanChuyen', N'COD', '0930001111', N'456 Trần Hưng Đạo', N'Trịnh Văn E');

-- ChiTietHoaDon (ĐÃ SỬA ID_SPCT)
INSERT INTO ChiTietHoaDon (ID_SPCT, MaHD, GiaXuat, SoLuongXuat, LoHang, ThanhTien) VALUES
('SP001_BLACK_S', 'HD001', 150000, 1, N'LH001', 150000), -- Sử dụng ID_SPCT hợp lệ
('SP001_WHITE_M', 'HD001', 150000, 1, N'LH001', 150000), -- Sử dụng ID_SPCT hợp lệ
('SP002_BLACK_S', 'HD002', 150000, 2, N'LH001', 300000), -- Sử dụng ID_SPCT hợp lệ
('SP002_WHITE_M', 'HD002', 150000, 1, N'LH001', 150000), -- Sử dụng ID_SPCT hợp lệ
('SP003_BLACK_L', 'HD003', 150000, 1, N'LH001', 150000), -- Sử dụng ID_SPCT hợp lệ
('SP004_BLUE_XL', 'HD004', 150000, 2, N'LH001', 300000); -- Sử dụng ID_SPCT hợp lệ


-- PhieuNhap
INSERT INTO PhieuNhap (MaPN, MaNV, TenNhaCungCap, NgayNhap) VALUES
('PN001', 'NV001', N'Công ty ABC', '2025-06-01'),
('PN002', 'NV002', N'Công ty Nike', '2025-06-05');

-- ChiTietPhieuNhap (ĐÃ SỬA ID_SPCT)
INSERT INTO ChiTietPhieuNhap (ID_SPCT, MaPN, GiaNhap, SoLuong, LoHang) VALUES
('SP001_BLACK_S', 'PN001', 100000, 50, N'LH001'), -- Sử dụng ID_SPCT hợp lệ
('SP005_BLACK_S', 'PN002', 100000, 10, N'LH001'); -- Sử dụng ID_SPCT hợp lệ

-- DiaChiNhanHang
INSERT INTO DiaChiNhanHang (MaKH, Ten_NguoiNhan, SoDT_NhanHang, DC_NhanHang) VALUES
('KH001', N'Nguyễn Văn Đạt', '0855022430', N'369,Tô Ký'),
('KH002', N'Phạm Thành Hưng', '0334043217', N'456 Trần Hưng Đạo, Q5, TP.HCM');