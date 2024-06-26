package com.example.prosushi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.prosushi.LoginAc;
import com.example.prosushi.Model.ModelNguoidung;
import com.example.prosushi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {
    DatabaseReference databaseReference;
    int smand;
    TextView tvtennd,tvpq,tvtenndtk,tvaddress,tvphone;
    Button btnlogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          View view = inflater.inflate(R.layout.fragment_user, container, false);
            tvpq=view.findViewById(R.id.tvphanquyen);
            tvtennd=view.findViewById(R.id.tvtennd);
            tvtenndtk=view.findViewById(R.id.tenndtk);
            tvaddress=view.findViewById(R.id.tvaddress);
            tvphone=view.findViewById(R.id.tvphone);
            btnlogout = view.findViewById(R.id.btnlogout);

          getmand();

        getnguoidung(smand);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginAc.class));
            }
        });
        return view;
    }

    public void getnguoidung(int mand){
        databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");

        databaseReference.orderByChild("mand").equalTo(mand).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelNguoidung modelNguoidung = dataSnapshot.getValue(ModelNguoidung.class);
                    tvtennd.setText("Tên tài khoản : "+modelNguoidung.getTaikhoan());
                    if (modelNguoidung.getRole()!=1){
                        tvpq.setText("Vai trò : Người dùng");
                    }else {
                        tvpq.setText("Vai trò : Admin");
                    }
                    tvtenndtk.setText("Tên : "+modelNguoidung.getTennguoidung());
                    tvaddress.setText("Địa chỉ : "+modelNguoidung.getDiachi());
                    tvphone.setText("Sdt : "+modelNguoidung.getSdt());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        
    }

    public void getmand(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("role", Context.MODE_PRIVATE);
        smand = sharedPreferences.getInt("mand",0);
    }


}