package com.example.prosushi.Model;

public class ModelHoadon {
    int mahd;
    int mand;
    String ngaytao;
    String tongtien;
    String trangthai;

    public ModelHoadon(int mahd, int mand, String ngaytao, String tongtien, String trangthai) {
        this.mahd = mahd;
        this.mand = mand;
        this.ngaytao = ngaytao;
        this.tongtien = tongtien;
        this.trangthai = trangthai;
    }

    public ModelHoadon(int mahd, int mand, String ngaytao, String tongtien) {
        this.mahd = mahd;
        this.mand = mand;
        this.ngaytao = ngaytao;
        this.tongtien = tongtien;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public ModelHoadon() {
    }

    public int getMahd() {
        return mahd;
    }

    public void setMahd(int mahd) {
        this.mahd = mahd;
    }

    public int getMand() {
        return mand;
    }

    public void setMand(int mand) {
        this.mand = mand;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

    public String getTongtien() {
        return tongtien;
    }

    public void setTongtien(String tongtien) {
        this.tongtien = tongtien;
    }
}
