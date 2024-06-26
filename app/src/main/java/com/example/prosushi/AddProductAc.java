package com.example.prosushi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prosushi.Model.ModelLoaisp;
import com.example.prosushi.Model.ModelSanpham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddProductAc extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    FirebaseDatabase database;
    // data loại spinner
    DatabaseReference databaseReference;
    // data hiện thị list dản phẩm
    DatabaseReference databaseReferenceSp;
    ArrayAdapter<String> adapter;
    ArrayList list;
    Spinner spinner;
    EditText edtMa,edtTen,edtGia;
    Button btThemsp;
    ImageView ivSuaAnh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtMa = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        btThemsp = findViewById(R.id.btThemsp);
        ivSuaAnh = findViewById(R.id.ivSuaAnh);

        // Quan Trọng
        mStorageRef = FirebaseStorage.getInstance().getReference("sushi");

        ivSuaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // hiện spinner từ danh sách firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("loaisp");
        list = new ArrayList<>();
        spinner = findViewById(R.id.spinner);

        adapter = new ArrayAdapter<String>(AddProductAc.this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        showSpinner();

        // them san pham lên firebase
        databaseReferenceSp = database.getReference("sanpham");

        btThemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // đưa mã tên giá loai vào sản phẩm

                String ma1 = edtMa.getText().toString().trim();
                String ten = edtTen.getText().toString().trim();
                String gia = edtGia.getText().toString().trim();
                int loai = spinner.getSelectedItemPosition();

                if( ma1.isEmpty() || ten.isEmpty() || gia.isEmpty()){
                    kiemtrasua();
                }else {
                    databaseReferenceSp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean kiem = false;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                int masp = ds.child("masp").getValue(int.class);
                                if (ma1.equals(String.valueOf(masp))) {
                                    kiem = true;
                                    break;
                                }
                            }
                            if (kiem) {
                                kiemtraloi();
                            } else {
                                ModelSanpham sanpham = new ModelSanpham(gia,loai,Integer.parseInt(ma1),ten);
                                themsp(sanpham);
                                uploadFile(sanpham);
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


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @com.google.firebase.database.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            ivSuaAnh.setImageURI(mImageUri);
        }
    }
    // up lên storage
    private void uploadFile(ModelSanpham sp) {
        if (mImageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference fileReference = mStorageRef.child("sanpham/" + System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                // Thêm đường dẫn của ảnh vào Firebase Realtime Database
                                String path = String.valueOf(sp.getMasp());
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sanpham").child(path);
                                databaseReference.child("anh").setValue(imageUrl);
                            });
                            progressDialog.dismiss();
                            Toast.makeText(AddProductAc.this, "Thêm sản phẩm thành công ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProductAc.this, "Thêm ảnh bị lỗi : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Không có firebase", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }


    // toolbar quay về trang chủ
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void themsp(ModelSanpham sanpham){

        // đặt vị trí cho san phẩm bằng mã loại
        String path = String.valueOf(sanpham.getMasp());
        // thêm sản phẩm
        databaseReferenceSp.child(path).setValue(sanpham, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                edtMa.setText("");
                edtTen.setText("");
                edtGia.setText("");
                Toast.makeText(AddProductAc.this, "Thêm sản phẩm thành công ", Toast.LENGTH_SHORT).show();
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
    private void kiemtraloi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo ");
        builder.setMessage(" Mã sản phẩm đã tồn tại ");
        builder.setNegativeButton("Ok",null);
        builder.show();
    }
    public void showSpinner()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name  = ds.child("tenloai").getValue(String.class);
                    list.add(name);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }
}