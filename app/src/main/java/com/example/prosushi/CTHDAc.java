package com.example.prosushi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosushi.Adapter.CTHDAdapter;
import com.example.prosushi.Model.ModelCTHD;
import com.example.prosushi.Model.ModelHoadon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CTHDAc extends AppCompatActivity {
    CTHDAdapter cthdAdapter;
    ArrayList<ModelCTHD> listct;
    ArrayList<ModelHoadon> listhd;
    ListView lvcthd;
    int smahd,smakh;
    String sngaytao,stongtien;
    TextView tvmahd,tvmakh,tvngaytao,tvtongtien;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cthdac);

        tvmahd = (TextView) findViewById(R.id.tvmahd);
        tvmakh = (TextView) findViewById(R.id.tvmakh);
        tvngaytao = (TextView) findViewById(R.id.tvngaytao);
        tvtongtien = (TextView) findViewById(R.id.tvtongtien);

        imgback = findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvcthd=findViewById(R.id.lvsp);

        getmahd();
        tvmahd.setText("Mã hóa đơn : "+smahd);
        tvmakh.setText("Mã người dùng : "+smakh);
        tvngaytao.setText("Ngày tạo : "+sngaytao);
        tvtongtien.setText("Thành tiền : "+ stongtien);
        getdatacthd(smahd);



        cthdAdapter = new CTHDAdapter(this,listct);
        lvcthd.setAdapter(cthdAdapter);
    }
    public void getmahd(){
        SharedPreferences getshare = getSharedPreferences("smahd", this.MODE_PRIVATE);
        smahd = getshare.getInt("sharemahd",0);
        smakh = getshare.getInt("sharemakh",0);
        sngaytao = getshare.getString("sharengaytao","");
        stongtien = getshare.getString("sharetongtien","");


    }
    public void getdatacthd(int mahd){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("cthd");
        listct = new ArrayList<>();

        databaseReference.orderByChild("mahd").equalTo(mahd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelCTHD modelCTHD = dataSnapshot.getValue(ModelCTHD.class);
                    listct.add(modelCTHD);
                }
                cthdAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}