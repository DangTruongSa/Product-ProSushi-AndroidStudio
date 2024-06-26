package com.example.prosushi.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prosushi.Fragment.CartFragment;
import com.example.prosushi.Model.ModelGiohang;
import com.example.prosushi.Model.ModelSanpham;
import com.example.prosushi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GiohangAdapter extends RecyclerView.Adapter<GiohangAdapter.Viewholder>{
    Context c;
    ArrayList<ModelGiohang> listgh;
    int tonggia=0;
    CartFragment cart;

    public GiohangAdapter(Context c, ArrayList<ModelGiohang> listgh) {
        this.c = c;
        this.listgh = listgh;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)c).getLayoutInflater();
        View vỉew = inflater.inflate(R.layout.oneitem_giohang,parent,false);


        return new Viewholder(vỉew);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        ModelGiohang modelGiohang = listgh.get(position);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("sanpham");
        databaseReference.child(String.valueOf(modelGiohang.getMasp())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String path = snapshot.child("anh").getValue(String.class);
                Picasso.get().load(path).into(holder.ivAnh);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        holder.tensp.setText(modelGiohang.getTensp());
        holder.soluong.setText("1");
        holder.giasp.setText("Giá: "+Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString())+" VND");

        tonggia = tonggia +Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString());

        holder.cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.soluong.setText(String.valueOf(Integer.valueOf(holder.soluong.getText().toString())+1));
                holder.giasp.setText("Giá: "+Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString())+" VND");

                tonggia = tonggia +Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString())-Integer.valueOf(modelGiohang.getGiasp())*(Integer.valueOf(holder.soluong.getText().toString())-1);

                Intent intent = new Intent("Tonggiagiohang");
                intent.putExtra("tonggia",tonggia);

                LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
            }
        });

        holder.tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(holder.soluong.getText().toString())==0){
                    holder.soluong.setText(String.valueOf(Integer.valueOf(holder.soluong.getText().toString())-0));
                    holder.giasp.setText("Giá: "+Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString())+" VND");
                }else {
                    holder.soluong.setText(String.valueOf(Integer.valueOf(holder.soluong.getText().toString())-1));
                    holder.giasp.setText("Giá: "+Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString())+" VND");

                    tonggia = tonggia - Integer.valueOf(modelGiohang.getGiasp());
                    Intent intent = new Intent("Tonggiagiohang");
                    intent.putExtra("tonggia",tonggia);

                    LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
                }
            }
        });

        holder.xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("giohang")
                        .child(String.valueOf(modelGiohang.getMasp()))
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(c, "Xóa thành công!!", Toast.LENGTH_SHORT).show();

                            }
                        });
                tonggia = 0;
                if (tonggia == 0){
                    tonggia = tonggia +Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString());
                    tonggia = tonggia -Integer.valueOf(modelGiohang.getGiasp())*Integer.valueOf(holder.soluong.getText().toString());
                }

                Intent intent = new Intent("Tonggiagiohang");
                intent.putExtra("tonggia",tonggia);

                LocalBroadcastManager.getInstance(c).sendBroadcast(intent);

            }
        });

//lấy tổng giá sp

        Intent intent = new Intent("Tonggiagiohang");
        intent.putExtra("tonggia",tonggia);

        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
       return listgh.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView tensp,giasp,soluong;
        ImageView cong,tru;
        Button xoa;
        ImageView ivAnh;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tensp=(TextView) itemView.findViewById(R.id.tvnameproduct);
            giasp=(TextView) itemView.findViewById(R.id.tvpriceproduct);
            soluong=(TextView) itemView.findViewById(R.id.tvcostproduct);
            cong=(ImageView) itemView.findViewById(R.id.imvaddproduct);
            tru=(ImageView) itemView.findViewById(R.id.imvminusproduct);
            xoa=(Button) itemView.findViewById(R.id.btndeleteproduct);
            ivAnh = itemView.findViewById(R.id.ivAnhGio);

        }
    }



}
