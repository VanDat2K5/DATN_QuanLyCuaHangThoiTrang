CREATE DATABASE DATN
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
('KH001', N'Nguyễn Văn A', 'vana@example.com', '0901234567'),
('KH002', N'Lê Thị B', 'leb@example.com', '0912345678');

-- NhanVien
INSERT INTO NhanVien (MaNV, TenNV, Email, SoDT)
VALUES 
('NV001', N'Trần Văn C', 'tranc@example.com', '0923456789');

-- TaiKhoan
INSERT INTO TaiKhoan (ID_MaNV, ID_MaKH, TenTK, MatKhau, HinhAnh)
VALUES 
('NV001', NULL, 'admin1', '123456', 'admin.jpg'),
(NULL, 'KH001', 'khach1', 'abc123', 'user1.jpg');

-- KhuyenMai
INSERT INTO KhuyenMai (MaKM, TenKM, Giam, GioiHan, NgayBatDau, NgayKetThuc)
VALUES 
('KM001', N'Giảm giá hè', 10, 100, '2025-06-01', '2025-06-30');

-- LoaiSanPham
INSERT INTO LoaiSanPham (MaLoaiSP, LoaiSP)
VALUES 
('LSP001', N'Áo thun'),
('LSP002', N'Quần jean');

-- SanPham
INSERT INTO SanPham (MaSP, MaLoaiSP, TenSP)
VALUES 
('SP001', 'LSP001', N'Áo thun nam cổ tròn'),
('SP002', 'LSP002', N'Quần jean nữ');

-- Mau
INSERT INTO Mau (MaMau, TenMau)
VALUES 
('M001', N'Đen'), 
('M002', N'Trắng');

-- Size
INSERT INTO Size (MaSize, TenSize)
VALUES 
('S', 'Small'),
('M', 'Medium');

-- ChiTietSanPham
INSERT INTO ChiTietSanPham (ID_SPCT, MaSP, MaMau, MaSize, SoLuong, GiaNhap, GiaXuat, LoHang)
VALUES 
('CTSP001', 'SP001', 'M001', 'M', 50, 80000, 120000, N'LH001'),
('CTSP002', 'SP002', 'M002', 'S', 30, 100000, 150000, N'LH002');

-- HinhAnh
INSERT INTO HinhAnh (MaSP, HinhAnh)
VALUES 
('SP001', 'aothun1.jpg'),
('SP002', 'quanjean1.jpg');

-- HoaDon
INSERT INTO HoaDon (MaHD, MaNV, MaKH, MaKM, TongTien, NgayLap, TrangThai, PT_ThanhToan, SoDT_NhanHang, DC_NhanHang, Ten_NguoiNhan)
VALUES 
('HD001', 'NV001', 'KH001', 'KM001', 240000, '2025-06-06', N'DaThanhToan', N'COD', '0901234567', N'123 Lê Lợi', N'Nguyễn Văn A');

-- ChiTietHoaDon
INSERT INTO ChiTietHoaDon (ID_SPCT, MaHD, GiaXuat, SoLuongXuat, LoHang, ThanhTien)
VALUES 
('CTSP001', 'HD001', 120000, 2, N'LH001', 240000);

-- PhieuNhap
INSERT INTO PhieuNhap (MaPN, MaNV, TenNhaCungCap, NgayNhap)
VALUES 
('PN001', 'NV001', N'Công ty ABC', '2025-06-01');

-- ChiTietPhieuNhap
INSERT INTO ChiTietPhieuNhap (ID_SPCT, MaPN, GiaNhap, SoLuong, LoHang)
VALUES 
('CTSP001', 'PN001', 80000, 50, N'LH001');

-- DiaChiNhanHang
INSERT INTO DiaChiNhanHang (MaKH, Ten_NguoiNhan, SoDT_NhanHang, DC_NhanHang)
VALUES 
('KH001', N'Nguyễn Văn A', '0901234567', N'123 Lê Lợi, Q1, TP.HCM');




-- KhachHang
INSERT INTO KhachHang (MaKH, TenKH, Email, SoDT)
VALUES 
('KH003', N'Nguyễn Văn Đạt', 'vandat05022005@gmail.com', '0855022430');

-- TaiKhoan
INSERT INTO TaiKhoan (ID_MaNV, ID_MaKH, TenTK, MatKhau, HinhAnh)
VALUES 
(NULL, 'KH003', 'vandat', '0502', 'user1.jpg');

-- HoaDon
INSERT INTO HoaDon (MaHD, MaNV, MaKH, MaKM, TongTien, NgayLap, TrangThai, PT_ThanhToan, SoDT_NhanHang, DC_NhanHang, Ten_NguoiNhan)
VALUES 
('HD002', 'NV001', 'KH003', 'KM001', 240000, '2025-06-06', N'DaThanhToan', N'Bank', '0855022430', N'369,Tô Ký', N'Nguyễn Văn Đạt');

-- ChiTietHoaDon
INSERT INTO ChiTietHoaDon (ID_SPCT, MaHD, GiaXuat, SoLuongXuat, LoHang, ThanhTien)
VALUES 
('CTSP001', 'HD002', 120000, 2, N'LH001', 240000),
('CTSP002', 'HD002', 120000, 2, N'LH001', 240000);

-- HoaDon
INSERT INTO HoaDon (MaHD, MaNV, MaKH, MaKM, TongTien, NgayLap, TrangThai, PT_ThanhToan, SoDT_NhanHang, DC_NhanHang, Ten_NguoiNhan)
VALUES 
('HD003', 'NV001', 'KH003', null, 240000, '2025-06-06', N'ChoXacNhan', N'Bank', '0855022430', N'369,Tô Ký', N'Nguyễn Văn Đạt');

-- ChiTietHoaDon
INSERT INTO ChiTietHoaDon (ID_SPCT, MaHD, GiaXuat, SoLuongXuat, LoHang, ThanhTien)
VALUES 
('CTSP001', 'HD003', 120000, 2, N'LH001', 120000),
('CTSP002', 'HD003', 120000, 2, N'LH001', 120000);



-- NhanVien
INSERT INTO NhanVien (MaNV, TenNV, Email, SoDT)
VALUES 
('NV002', N'Trần Văn C', 'tranc1@example.com', '0923456789');

-- TaiKhoan
INSERT INTO TaiKhoan (ID_MaNV, ID_MaKH, TenTK, MatKhau, HinhAnh)
VALUES 
('NV002', NULL, 'admin', '1111', 'admin.jpg');


