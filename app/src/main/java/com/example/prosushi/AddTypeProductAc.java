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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosushi.Model.ModelLoaisp;
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

public class AddTypeProductAc extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;

    EditText edtId;
    EditText edtTen;
    TextView tvThemAnh;
    ImageView mImageView;
    Button btThem;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type_product);

        edtId = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        btThem = findViewById(R.id.btThemloai);
        mImageView=findViewById(R.id.ivSuaAnh);


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Quan Trọng
        mStorageRef = FirebaseStorage.getInstance().getReference("sushi");

        //hiện toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // thêm loại lên firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("loaisp");
        // kiểm tra xem có trùng loại kông

        btThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lấy mã và tên đưa vào loại
                String ma = edtId.getText().toString().trim();
                String ten = edtTen.getText().toString().trim();

                // thêm loại
                if (ma.isEmpty() || ten.isEmpty()){
                    kiemtrasua();
                }else {

                    // so sánh nếu mã trùng với mã đã có hiện thông báo
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean kiem = false;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                int maloai = ds.child("maloai").getValue(int.class);
                                if (ma.equals(String.valueOf(maloai))) {
                                    kiem = true;
                                    break;
                                }
                            }
                            if (kiem) {
                                kiemtraloi();
                            } else {
                                ModelLoaisp loai = new ModelLoaisp(Integer.parseInt(ma),ten);
                                themLoai(loai);
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

    // toolbar quay về trang chủ
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // thêm loại sản phẩm
    public void themLoai(ModelLoaisp loai){
        String path = String.valueOf(loai.getMaloai());
            databaseReference.child(path).setValue(loai, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    edtId.setText("");
                    edtTen.setText("");
                    Toast.makeText(AddTypeProductAc.this, "Thêm loại thành công ", Toast.LENGTH_SHORT).show();

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
        builder.setMessage("Mã loại đã tồn tại ");
        builder.setNegativeButton("Ok",null);
        builder.show();
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
            mImageView.setImageURI(mImageUri);
        }
    }

    private void uploadFile(ModelLoaisp loai) {
        if (mImageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference fileReference = mStorageRef.child("loaisp/" + System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                // Thêm đường dẫn của ảnh vào Firebase Realtime Database
                                String path = String.valueOf(loai.getMaloai());
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("loaisp").child(path);
                                databaseReference.child("anh").setValue(imageUrl);
                            });
                            progressDialog.dismiss();
                            Toast.makeText(AddTypeProductAc.this, "Thêm Loại Thành Công ", Toast.LENGTH_LONG).show();
                        }})
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddTypeProductAc.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }
}