package com.example.prosushi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prosushi.CTHDAc;
import com.example.prosushi.LoginAc;
import com.example.prosushi.MainActivity;
import com.example.prosushi.Model.ModelHoadon;
import com.example.prosushi.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Viewholder>{

    Context c;
    ArrayList<ModelHoadon> listhd;

    public HistoryAdapter(Context c, ArrayList<ModelHoadon> listhd) {
        this.c = c;
        this.listhd = listhd;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)c).getLayoutInflater();
        View view = inflater.inflate(R.layout.oneitem_hoadon,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ModelHoadon modelHoadon = listhd.get(position);

        holder.tvmahd.setText("Mã hóa đơn : "+modelHoadon.getMahd()+"");
        holder.tvmakh.setText("Mã khách hàng : "+modelHoadon.getMand()+"");
        holder.tvngaytao.setText("Ngày tạo : "+modelHoadon.getNgaytao());
        holder.tvtongtien.setText(modelHoadon.getTongtien()+" VND");
        holder.tvTrangThai.setText("Trạng thái : "+modelHoadon.getTrangthai());

        SharedPreferences getshare = c.getSharedPreferences("role",Context.MODE_PRIVATE);
        int role = getshare.getInt("rolekey",0);

        int mahd = modelHoadon.getMahd();
        int makh = modelHoadon.getMand();
        String ngaytao = modelHoadon.getNgaytao();
        String tongtien = modelHoadon.getTongtien();

        holder.btGiao.setVisibility(View.INVISIBLE);


        if(role == 1){
            String trangthaiHuy = "Đơn hàng đã hủy";
            String trangthaiX = "Đơn hàng đã được xác nhận";
            String trangthaiGiao = "Đơn hàng đang vận chuyển ";
            String trangthaiV = "Đã nhận được hàng";

            holder.btHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ModelHoadon hd = new ModelHoadon(mahd,makh,ngaytao,tongtien,trangthaiHuy);
                    holder.TrangThai(hd);
                    holder.tvTrangThai.setBackgroundColor(Color.parseColor("#F44336"));

                }
            });
            holder.btGiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ModelHoadon hd = new ModelHoadon(mahd,makh,ngaytao,tongtien,trangthaiGiao);
                    holder.tvTrangThai.setBackgroundColor(Color.parseColor("#4CAF50"));
                    holder.TrangThai(hd);
                }
            });
            holder.btXacNhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ModelHoadon hd = new ModelHoadon(mahd,makh,ngaytao,tongtien,trangthaiX);
                    holder.TrangThai(hd);
                    holder.tvTrangThai.setBackgroundColor(Color.parseColor("#FF9800"));
                }
            });

            if(modelHoadon.getTrangthai().equals(trangthaiHuy)){
                holder.btHuy.setVisibility(View.INVISIBLE);
                holder.btXacNhan.setVisibility(View.INVISIBLE);
                holder.btGiao.setVisibility(View.INVISIBLE);
            }else if(modelHoadon.getTrangthai().equals(trangthaiGiao)){
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.btGiao.setVisibility(View.INVISIBLE);
                holder.btXacNhan.setVisibility(View.INVISIBLE);
            }else if (modelHoadon.getTrangthai().equals(trangthaiX)){
                holder.btGiao.setVisibility(View.VISIBLE);
                holder.btXacNhan.setVisibility(View.INVISIBLE);
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#FF9800"));
            }else if(modelHoadon.getTrangthai().equals(trangthaiV)){
                holder.btHuy.setVisibility(View.INVISIBLE);
                holder.btXacNhan.setVisibility(View.INVISIBLE);
                holder.btGiao.setVisibility(View.INVISIBLE);
            }

        }else if(role ==2 ){
            String trangthaiHuy = "Đơn hàng đã hủy";
            String trangthaiX = "Đã nhận được hàng";

            holder.btGiao.setVisibility(View.INVISIBLE);
            holder.btHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelHoadon hd = new ModelHoadon(mahd,makh,ngaytao,tongtien,trangthaiHuy);
                    holder.tvTrangThai.setBackgroundColor(Color.parseColor("#F44336"));
                    holder.TrangThai(hd);

                }
            });

            holder.btXacNhan.setText("Đã nhận hàng ");
            holder.btXacNhan.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.btXacNhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ModelHoadon hd = new ModelHoadon(mahd,makh,ngaytao,tongtien,trangthaiX);
                    holder.tvTrangThai.setBackgroundColor(Color.parseColor("#4CAF50"));
                    holder.TrangThai(hd);

                }
            });
            holder.btXacNhan.setVisibility(View.INVISIBLE);
            String trangthaiGiao = "Đơn hàng đang vận chuyển ";

            if(modelHoadon.getTrangthai().equals(trangthaiHuy)){
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#F44336"));
                holder.btHuy.setVisibility(View.INVISIBLE);
                holder.btXacNhan.setVisibility(View.INVISIBLE);
            }else if(modelHoadon.getTrangthai().equals(trangthaiX)) {
                holder.tvTrangThai.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.btHuy.setVisibility(View.INVISIBLE);
                holder.btXacNhan.setVisibility(View.INVISIBLE);
            }else if(modelHoadon.getTrangthai().equals(trangthaiGiao)){
                holder.btXacNhan.setVisibility(View.VISIBLE);
            }
        }



        holder.tvpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharemahd = c.getSharedPreferences("smahd",Context.MODE_PRIVATE);
                SharedPreferences.Editor getmahds = sharemahd.edit();
                getmahds.putInt("sharemahd",modelHoadon.getMahd()); //paste getRole() vao 1
                getmahds.putInt("sharemakh",modelHoadon.getMand());
                getmahds.putString("sharengaytao",modelHoadon.getNgaytao());
                getmahds.putString("sharetongtien",modelHoadon.getTongtien());
                getmahds.apply();

                c.startActivity(new Intent(c, CTHDAc.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listhd.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        Button btXacNhan,btGiao,btHuy;
        TextView tvTrangThai;
        DatabaseReference databaseReference ;
        TextView tvmahd,tvmakh,tvngaytao,tvtongtien,tvpress;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btXacNhan = itemView.findViewById(R.id.btXacNhan);
            btGiao = itemView.findViewById(R.id.btGiaoHang);
            btHuy = itemView.findViewById(R.id.btHuy);

            databaseReference = FirebaseDatabase.getInstance().getReference("hoadon");
            tvmahd=(TextView) itemView.findViewById(R.id.tvmahd);
            tvmakh=(TextView) itemView.findViewById(R.id.tvmakh);
            tvngaytao=(TextView) itemView.findViewById(R.id.tvngaytao);
            tvtongtien=(TextView) itemView.findViewById(R.id.tvtongtien);
            tvpress=(TextView) itemView.findViewById(R.id.tvpress);


        }
        public void TrangThai (ModelHoadon hoadon){
            String path = String.valueOf(hoadon.getMahd());
            databaseReference.child(path).setValue(hoadon, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                }
            });
        }
    }

}
