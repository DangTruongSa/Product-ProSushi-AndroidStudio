package com.example.prosushi.Model;

import java.util.HashMap;
import java.util.Map;

public class ModelSanpham {
    String giasp;
    int maloai;
    int masp;
    String tensp;

    public ModelSanpham() {
    }

    public ModelSanpham(String giasp, int maloai, int masp, String tensp) {
        this.giasp = giasp;
        this.maloai = maloai;
        this.masp = masp;
        this.tensp = tensp;
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

    public Map<String ,Object> toMap(){
        HashMap<String ,Object> result = new HashMap<>();
        result.put("tensp",tensp);
        result.put("giasp",giasp);
        result.put("maloai",maloai);
        return  result;
    }
}
