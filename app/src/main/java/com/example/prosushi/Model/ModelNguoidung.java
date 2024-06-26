package com.example.prosushi.Model;

public class ModelNguoidung {
    String diachi;
    int mand;
    String matkhau;
    int role;
    String sdt;
    String taikhoan;
    String tennguoidung;
    public ModelNguoidung() {
    }
    public ModelNguoidung(String diachi, int mand, String matkhau, int role, String sdt, String taikhoan, String tennguoidung) {
        this.diachi = diachi;
        this.mand = mand;
        this.matkhau = matkhau;
        this.role = role;
        this.sdt = sdt;
        this.taikhoan = taikhoan;
        this.tennguoidung = tennguoidung;
    }



    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public int getMand() {
        return mand;
    }

    public void setMand(int mand) {
        this.mand = mand;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTaikhoan() {
        return taikhoan;
    }

    public void setTaikhoan(String taikhoan) {
        this.taikhoan = taikhoan;
    }

    public String getTennguoidung() {
        return tennguoidung;
    }

    public void setTennguoidung(String tennguoidung) {
        this.tennguoidung = tennguoidung;
    }
}
