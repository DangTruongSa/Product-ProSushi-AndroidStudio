package com.example.prosushi.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prosushi.Model.ModelLoaisp;
import com.example.prosushi.Model.ModelSanpham;
import com.example.prosushi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewholder>{

    Context c;
    ArrayList<ModelSanpham> list;

    private ProductAdapter.myInterfaceSp myinterfacesp;

    public ProductAdapter(Context c, ArrayList<ModelSanpham> list, myInterfaceSp myinterfacesp) {
        this.c = c;
        this.list = list;
        this.myinterfacesp = myinterfacesp;
    }

    public interface myInterfaceSp{
        void suasanpham(ModelSanpham sp);
        void xoasanpham(ModelSanpham sp);
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)c).getLayoutInflater();
        View view = inflater.inflate(R.layout.oneitem_sanpham,parent,false);


        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvtensp.setText(list.get(position).getTensp());
        holder.tvgiasp.setText(list.get(position).getGiasp()+" VND");

        ModelSanpham sp = list.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("sanpham");
        databaseReference.child(String.valueOf(sp.getMasp())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String path = snapshot.child("anh").getValue(String.class);
                Picasso.get().load(path).into(holder.ivAnh);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        SharedPreferences getshare = c.getSharedPreferences("role",Context.MODE_PRIVATE);
        int role = getshare.getInt("rolekey",0);

        switch (role){
            case 1:
                holder.btnadd.setText("Sửa");
                holder.btnmua.setText("Xóa");

                holder.btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myinterfacesp.suasanpham(list.get(position));
                    }
                });

                holder.btnmua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myinterfacesp.xoasanpham(list.get(position));
                    }
                });
                break;
            case 2:
                holder.btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("giohang");
                        ModelSanpham modelSanpham = new ModelSanpham(list.get(position).getGiasp(),
                                list.get(position).getMaloai(),
                                list.get(position).getMasp(),
                                list.get(position).getTensp());

                        databaseReference.child(String.valueOf(list.get(position).getMasp())).setValue(modelSanpham);

                        Toast.makeText(c, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.btnmua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code
                    }
                });

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        TextView tvtensp,tvgiasp;
        Button btnadd,btnmua;
        ImageView ivAnh;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvtensp=(TextView) itemView.findViewById(R.id.tvtensp);
            tvgiasp=(TextView) itemView.findViewById(R.id.tvgiasp);
            btnadd=(Button) itemView.findViewById(R.id.btnadd);
            btnmua=(Button) itemView.findViewById(R.id.btnmua);

            ivAnh = itemView.findViewById(R.id.ivAnh);
        }
    }
}