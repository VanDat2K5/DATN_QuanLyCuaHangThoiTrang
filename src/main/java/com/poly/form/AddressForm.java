package com.poly.form;

import com.poly.entity.DiaChiNhanHang;
import com.poly.entity.KhachHang;

public class AddressForm {
    private String tenNguoiNhan;
    private String soDTNhanHang;
    private String dcNhanHang;

    public String getTenNguoiNhan() { return tenNguoiNhan; }
    public void setTenNguoiNhan(String tenNguoiNhan) { this.tenNguoiNhan = tenNguoiNhan; }
    public String getSoDTNhanHang() { return soDTNhanHang; }
    public void setSoDTNhanHang(String soDTNhanHang) { this.soDTNhanHang = soDTNhanHang; }
    public String getDcNhanHang() { return dcNhanHang; }
    public void setDcNhanHang(String dcNhanHang) { this.dcNhanHang = dcNhanHang; }

    public DiaChiNhanHang toEntity(KhachHang kh) {
        DiaChiNhanHang dc = new DiaChiNhanHang();
        dc.setKhachHang(kh);
        dc.setTenNguoiNhan(tenNguoiNhan);
        dc.setSoDTNhanHang(soDTNhanHang);
        dc.setDcNhanHang(dcNhanHang);
        return dc;
    }
} 