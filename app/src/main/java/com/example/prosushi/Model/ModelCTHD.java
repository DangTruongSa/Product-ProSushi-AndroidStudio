package com.example.prosushi.Model;

public class ModelCTHD {
    int macthd,mahd,masp,soluong,thanhtien;

    public ModelCTHD() {
    }

    public ModelCTHD(int macthd, int mahd, int masp, int soluong, int thanhtien) {
        this.macthd = macthd;
        this.mahd = mahd;
        this.masp = masp;
        this.soluong = soluong;
        this.thanhtien = thanhtien;
    }

    public int getMacthd() {
        return macthd;
    }

    public void setMacthd(int macthd) {
        this.macthd = macthd;
    }

    public int getMahd() {
        return mahd;
    }

    public void setMahd(int mahd) {
        this.mahd = mahd;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }
}
