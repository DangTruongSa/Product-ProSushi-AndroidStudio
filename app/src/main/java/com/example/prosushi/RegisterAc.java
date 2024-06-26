package com.example.prosushi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prosushi.Model.ModelLoaisp;
import com.example.prosushi.Model.ModelNguoidung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterAc extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    private EditText edtUsername, edtPassword, edtRePassword, edtName, edtAddress, edtNumPhone;
    private Button btnBack, btnRegister;
    ArrayList<String> list;
    ImageView ivback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ivback=findViewById(R.id.ivback);
        edtAddress = findViewById(R.id.edtAddress);
        edtUsername = findViewById(R.id.edtUsername);
        edtNumPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnRegister = findViewById(R.id.btnDangKy);

        reference = FirebaseDatabase.getInstance().getReference().child("nguoidung");

        list = new ArrayList();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    int mand = ds.child("mand").getValue(int.class);
                    list.add(String.valueOf(mand));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("nguoidung");

                String pass = edtPassword.getText().toString();
                String rePass = edtRePassword.getText().toString();
                String username = edtUsername.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtNumPhone.getText().toString();
                String name = edtName.getText().toString();

                if(pass.isEmpty() || rePass.isEmpty() || username.isEmpty() || address.isEmpty() || phone.isEmpty() || name.isEmpty()){
                    kiemtrasua();
                }else {
                    if(pass.equals(rePass)){
                        ModelNguoidung nguoidung = new ModelNguoidung(address,list.size(),pass,2,phone,username,name);
                        themNd(nguoidung);
                        SharedPreferences sharedPreferences = getSharedPreferences("tkmk",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("tk",username);
                        editor.putString("mk",pass);
                        editor.apply();
                        startActivity(new Intent(RegisterAc.this, LoginAc.class));
                        finish();
                    }else{
                        kiemtra();
                    }
                }


            }
        });

        /* muốn tạo vị trí người dùng đưa vào list rồi list.size +1

         */
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void themNd(ModelNguoidung nguoidung){


        String path = String.valueOf(list.size());


        reference.child(path).setValue(nguoidung, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(RegisterAc.this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void kiemtrasua(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo ");
        builder.setMessage("Vui lòng nhập đầy đủ thông tin  ");
        builder.setNegativeButton("Ok",null);
        builder.show();
    }

    private void kiemtra(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo ");
        builder.setMessage("Vui lòng nhập mật khẩu giống nhau  ");
        builder.setNegativeButton("Ok",null);
        builder.show();
    }
}