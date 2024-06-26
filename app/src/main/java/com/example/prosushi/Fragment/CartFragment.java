package com.example.prosushi.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosushi.Adapter.GiohangAdapter;
import com.example.prosushi.AddTypeProductAc;
import com.example.prosushi.Model.ModelCTHD;
import com.example.prosushi.Model.ModelGiohang;
import com.example.prosushi.Model.ModelHoadon;
import com.example.prosushi.Model.ModelLoaisp;
import com.example.prosushi.Model.ModelSanpham;
import com.example.prosushi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    TextView tvtongtien;
    Button btnbuy;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    GiohangAdapter giohangAdapter;
    int tonggia=1;
    int mahdcc,macthdcc,ma1,ma2;

    ArrayList<ModelGiohang> listgh;
    ArrayList<ModelHoadon> listhd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView=view.findViewById(R.id.rcyv1);
        tvtongtien=view.findViewById(R.id.tvtongtien);
        btnbuy=view.findViewById(R.id.btnbuy);

        listhd = new ArrayList<>();
        DatabaseReference referenceHoadon = FirebaseDatabase.getInstance().getReference("hoadon");

        referenceHoadon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listhd.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelHoadon hoadon = dataSnapshot.getValue(ModelHoadon.class);
                    listhd.add(hoadon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        loaddatagiohang();
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mBroadcastReceiver,new IntentFilter("Tonggiagiohang"));

        loaddatarecycleview();

        Calendar calendar = Calendar.getInstance();
        String getdaytime = DateFormat.getDateInstance().format(calendar.getTime());

        String trangthai = "Đang chờ xác nhận";
        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("hoadon");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            int mahd = dataSnapshot.child("mahd").getValue(int.class);
                            mahdcc = mahd;
                        }
                        for (int i = 0 ; i<=mahdcc;i++){
                            ma1 = i;
                        }
                        ModelHoadon hoadon = new ModelHoadon(ma1+1,1,getdaytime,String.valueOf(tonggia),trangthai);
                        themhd(hoadon);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//demo cthd
//                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("cthd");
//                    reference1.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                int macthd = dataSnapshot.child("macthd").getValue(int.class);
//                                macthdcc = macthd;
//                            }
//                            for (int b=0;b<listgh.size();b++){
//
//                                ModelCTHD cthd = new ModelCTHD(listgh.size()+1,listhd.size()+1,listgh.get(b).getMasp(),1,Integer.parseInt(listgh.get(b).getGiasp()));
//                                themcthd(cthd);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
            }
        });

        return view;
    }

    public void themhd(ModelHoadon hd){
        String path = String.valueOf(ma1+1);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("hoadon");
        reference.child(path).setValue(hd, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Mua hàng thành công ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void themcthd(ModelCTHD cthd){
        String path = String.valueOf(ma2);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cthd");
        reference.child(path).setValue(cthd, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }



    public void loaddatagiohang(){

        database= FirebaseDatabase.getInstance();
        databaseReference = database.getReference("giohang");

        listgh= new ArrayList<>();

        //add arraylist voi firebase

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listgh.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelGiohang giohang = dataSnapshot.getValue(ModelGiohang.class);
                    listgh.add(giohang);
                }
                giohangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loaddatarecycleview(){
        giohangAdapter=new GiohangAdapter(getContext(),listgh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(giohangAdapter);
    }
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tonggia = intent.getIntExtra("tonggia",0);
            tvtongtien.setText(tonggia+" VND");
        }
    };

}