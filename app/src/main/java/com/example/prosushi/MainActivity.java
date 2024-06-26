package com.example.prosushi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosushi.Fragment.CartFragment;
import com.example.prosushi.Fragment.HistoryFragment;
import com.example.prosushi.Fragment.HomeFragment;
import com.example.prosushi.Fragment.UserFragment;
import com.example.prosushi.Model.ModelSanpham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {



    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment=new HomeFragment();
    CartFragment cartFragment=new CartFragment();
    HistoryFragment historyFragment=new HistoryFragment();
    UserFragment userFragment=new UserFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView=findViewById(R.id.btnvgt);

        Menu nav_menu = bottomNavigationView.getMenu();

        SharedPreferences getshare = getSharedPreferences("role",MODE_PRIVATE);
        int role = getshare.getInt("rolekey",0);

        switch (role){
            case 0:
                Toast.makeText(this, "ko lay duoc key", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                nav_menu.findItem(R.id.madd).setVisible(true);
                nav_menu.findItem(R.id.mcart).setVisible(false);
                nav_menu.findItem(R.id.mhome).setVisible(true);
                nav_menu.findItem(R.id.mhistory).setVisible(true);
                nav_menu.findItem(R.id.muser).setVisible(true);
                break;
            case 2:
                nav_menu.findItem(R.id.madd).setVisible(false);
                nav_menu.findItem(R.id.mcart).setVisible(true);
                nav_menu.findItem(R.id.mhome).setVisible(true);
                nav_menu.findItem(R.id.mhistory).setVisible(true);
                nav_menu.findItem(R.id.muser).setVisible(true);
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.mhome){
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,homeFragment).commit();
                    return true;
                } else if (item.getItemId()==R.id.mcart) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,cartFragment).commit();
                    return true;
                } else if (item.getItemId()==R.id.mhistory) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,historyFragment).commit();
                    return true;
                } else if (item.getItemId()==R.id.muser) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,userFragment).commit();
                    return true;
                }else if (item.getItemId()==R.id.madd){
                    showDinalogThem();
                }
                return false;
            }
        });
    }
    private void showDinalogThem(){
        // hiện dinalog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dinalogthem,null);
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
                Intent intent = new Intent(MainActivity.this, AddProductAc.class);
                startActivity(intent);
            }
        });

        // di chuyển sang thêm loại
        tvThemloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTypeProductAc.class);
                startActivity(intent);
            }
        });
    }

    

}
