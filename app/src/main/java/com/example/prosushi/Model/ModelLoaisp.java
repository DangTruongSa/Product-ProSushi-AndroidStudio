package com.example.prosushi.Model;

import java.util.HashMap;
import java.util.Map;

public class ModelLoaisp {

    int maloai;
    String tenloai;
    String anh;

    public ModelLoaisp() {
    }

    public ModelLoaisp(int maloai, String tenloai) {
        this.maloai = maloai;
        this.tenloai = tenloai;
    }

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public Map<String ,Object> toMap(){
        HashMap<String ,Object> result = new HashMap<>();
        result.put("tenloai",tenloai);
        return  result;
    }
}
