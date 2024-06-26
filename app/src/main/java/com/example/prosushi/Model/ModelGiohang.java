package com.example.prosushi.Model;

public class ModelGiohang {

    String giasp;
    int maloai;
    int masp;
    String tensp;


    public ModelGiohang(String giasp, int maloai, int masp, String tensp) {
        this.giasp = giasp;
        this.maloai = maloai;
        this.masp = masp;
        this.tensp = tensp;
    }

    public ModelGiohang() {
    }

    public String getGiasp() {
        return giasp;
    }

    public void setGiasp(String giasp) {
        this.giasp = giasp;
    }

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }
}
