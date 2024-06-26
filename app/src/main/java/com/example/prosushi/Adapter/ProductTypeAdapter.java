package com.example.prosushi.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prosushi.Model.ModelLoaisp;
import com.example.prosushi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.MyViewHolder>{

    Context c;
    ArrayList<ModelLoaisp> list;

    private myInterfaceL myInterfaceL;

    public ProductTypeAdapter(Context c, ArrayList<ModelLoaisp> list, ProductTypeAdapter.myInterfaceL myInterfaceL) {
        this.c = c;
        this.list = list;
        this.myInterfaceL = myInterfaceL;
    }

    public interface myInterfaceL{
        void sualoaisp(ModelLoaisp modelloai);
        void xoaloaisp(ModelLoaisp modelLoai);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)c).getLayoutInflater();
        View view = inflater.inflate(R.layout.oneitem_loaisp,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ModelLoaisp loai= list.get(position);

        SharedPreferences getshare = c.getSharedPreferences("role",Context.MODE_PRIVATE);
        int role = getshare.getInt("rolekey",0);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("loaisp");
        databaseReference.child(String.valueOf(loai.getMaloai())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String path = snapshot.child("anh").getValue(String.class);
                Picasso.get().load(path).into(holder.ivloaisp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        holder.tvtenloai.setText(list.get(position).getTenloai());

        switch (role){
            case 1:




                holder.tvtenloai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showdialoguppdate(list.get(position).getMaloai());
                    }
                });

                holder.ivloaisp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showdialoguppdate(list.get(position).getMaloai());
                    }
                });
                break;
            case 2:
                break;
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivloaisp;
        TextView tvtenloai;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtenloai=(TextView) itemView.findViewById(R.id.tvtenloai);
            ivloaisp=(ImageView) itemView.findViewById(R.id.imageView8);
        }
    }

    private void showdialoguppdate(int position){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(c);
        LayoutInflater inflater = ((Activity)c).getLayoutInflater();
        View view = inflater.inflate(R.layout.dinalog_event_loaisp,null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        ImageView ivExit = view.findViewById(R.id.ivExit);
        TextView tvThemsanpham = view.findViewById(R.id.tvThemsanpham);
        TextView tvThemloai = view.findViewById(R.id.tvThemloai);

        // thoát dinalog
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // di chuyển sang them sản phẩm
        tvThemsanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterfaceL.xoaloaisp(list.get(position));
            }
        });

        // di chuyển sang thêm loại
        tvThemloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterfaceL.sualoaisp(list.get(position));
            }
        });

    }


}
