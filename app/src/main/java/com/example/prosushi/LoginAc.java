package com.example.prosushi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosushi.Model.ModelNguoidung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginAc extends AppCompatActivity {

    //firebse is Off
    Button btnlogin, nd1, nd2;
    EditText edttk, edtmk;
    TextView register;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.registera);
        btnlogin = findViewById(R.id.btnlogin);
        edttk = findViewById(R.id.edttk);
        edtmk = findViewById(R.id.edtmk);

        edtmk.setText("");
        edttk.setText("");

        SharedPreferences getshare = this.getSharedPreferences("tkmk", Context.MODE_PRIVATE);
        String taikhoan = getshare.getString("tk","");
        String matkhau = getshare.getString("mk","");
        edtmk.setText(matkhau);
        edttk.setText(taikhoan);



        if (!kiemtraInternet()) {
            // Nếu không có kết nối, hiển thị thông báo cho người dùng và đóng ứng dụng
            showNoInternetDialog();
        }

        // sang đăng ký
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAc.this, RegisterAc.class));
            }
        });
        // đăng nhâpk
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tk = edttk.getText().toString();
                String mk = edtmk.getText().toString();

                if (tk.isEmpty() || mk.isEmpty()) {
                    kiemtrasua();
                } else {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("nguoidung");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            boolean kiem = false;
                            int role = 0;
                            int mand = 0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String taikhoan = ds.child("taikhoan").getValue(String.class);
                                String matkhau = ds.child("matkhau").getValue(String.class);
                                role = ds.child("role").getValue(int.class);
                                mand = ds.child("mand").getValue(int.class);

                                if (tk.equals(taikhoan) && mk.equals(matkhau)) {
                                    kiem = true;
                                    break;

                                }
                            }
                            if (kiem) {
                                if (role == 1) {
                                    SharedPreferences sharerole = getSharedPreferences("role", MODE_PRIVATE);
                                    SharedPreferences.Editor editrole = sharerole.edit();
                                    editrole.putInt("rolekey", 1); //paste getRole() vao 1
                                    editrole.putInt("mand", mand);
                                    editrole.apply();

                                    startActivity(new Intent(LoginAc.this, MainActivity.class));
                                } else if (role == 2) {
                                    SharedPreferences sharerole = getSharedPreferences("role", MODE_PRIVATE);
                                    SharedPreferences.Editor editrole = sharerole.edit();
                                    editrole.putInt("rolekey", 2); //paste getRole() vao 1
                                    editrole.putInt("mand", mand);
                                    editrole.apply();

                                    startActivity(new Intent(LoginAc.this, MainActivity.class));
                                }

                            } else {
                                kiemtrataikhoan();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }


            }
        });

    }

    private void kiemtrasua() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo ");
        builder.setMessage("Vui lòng nhập đầy đủ thông tin  ");
        builder.setNegativeButton("Ok", null);
        builder.show();
    }

    private void kiemtrataikhoan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo ");
        builder.setMessage("Sai tài khoản hoặc mật khẩu   ");
        builder.setNegativeButton("Ok", null);
        builder.show();
    }

    private boolean kiemtraInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // Phương thức để hiển thị thông báo khi không có kết nối Internet và đóng ứng dụng
    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Không có kết nối Internet")
                .setMessage("Vui lòng kiểm tra kết nối ")
                .setCancelable(false)
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Khi người dùng nhấn nút "Đóng ứng dụng", đóng ứng dụng
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}