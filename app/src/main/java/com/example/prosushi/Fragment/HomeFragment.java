package com.example.prosushi.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.prosushi.Adapter.ProductAdapter;
import com.example.prosushi.Adapter.ProductTypeAdapter;
import com.example.prosushi.MainActivity;
import com.example.prosushi.Model.ModelLoaisp;
import com.example.prosushi.Model.ModelSanpham;
import com.example.prosushi.R;
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


public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    RecyclerView rcyvsanpham,rcyvloaisp;
    ImageSwitcher imgswitcher;
    ImageButton next,before;
    int index=0;
    int manganh[]={R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4};

    ArrayList<ModelLoaisp> list;
    ArrayList<ModelSanpham> listsp;
    ProductAdapter productAdapter;
    ProductTypeAdapter productTypeAdapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    //    ArrayList<String> listmasp;
    ImageView ivSuaAnh;
    ImageView ivSuaLoai;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        // Quan trọng
        mStorageRef = FirebaseStorage.getInstance().getReference("sushi");


        before=view.findViewById(R.id.before);
        next=view.findViewById(R.id.next);
        imgswitcher=view.findViewById(R.id.imgswitcher);

        rcyvloaisp=view.findViewById(R.id.gvloaisp);
        rcyvsanpham=view.findViewById(R.id.gvsanpham);

        slideshow();

        loaddataloaisp();

        loaddatasanpham();

        loadviewsanpham();

        databaseReference.removeEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelSanpham sp = snapshot.getValue(ModelSanpham.class);
                if(sp == null || list == null || list.isEmpty()){
                    return;
                }
                for(int i= 0; i<list.size();i++){
                    if(sp.getMaloai() == list.get(i).getMaloai()){
                        list.remove(list.get(i));
                    }
                    productAdapter.notifyDataSetChanged();
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view ;

    }
    public void loaddatasanpham(){

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("sanpham");

        listsp = new ArrayList<>();

        loadviewsanpham();

        //add arraylist voi firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listsp.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelSanpham sanpham = dataSnapshot.getValue(ModelSanpham.class);
                    listsp.add(sanpham);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void loaddataloaisp(){
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("loaisp");
        list = new ArrayList<>();
        loadviewloaisp();
        //add arraylist voi firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelLoaisp loaisp = dataSnapshot.getValue(ModelLoaisp.class);
                    list.add(loaisp);
                }
                productTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void slideshow(){

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --index;
                if (index<0){
                    index=manganh.length-1;
                }
                imgswitcher.setImageResource(manganh[index]);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if(index==manganh.length){
                    index=0;
                }
                imgswitcher.setImageResource(manganh[index]);
            }
        });

        imgswitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                return imageView;
            }
        });

        autoswitch();

        imgswitcher.setImageResource(manganh[index]);
    }
    public void autoswitch(){
        CountDownTimer timer = new CountDownTimer(1000000000,10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                index++;
                if(index==manganh.length){
                    index=0;
                }
                imgswitcher.setImageResource(manganh[index]);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    public void loadviewsanpham(){
        productAdapter=new ProductAdapter(getContext(), listsp, new ProductAdapter.myInterfaceSp() {
            @Override
            public void suasanpham(ModelSanpham sp) {
                showSuasp(sp);
            }

            @Override
            public void xoasanpham(ModelSanpham sp) {
                xoasp(sp);
            }
        });
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(),2);
        rcyvsanpham.setLayoutManager(gridLayoutManager);
        rcyvsanpham.setAdapter(productAdapter);
    }

    public void loadviewloaisp(){
        productTypeAdapter =new ProductTypeAdapter(getContext(), list, new ProductTypeAdapter.myInterfaceL() {
            @Override
            public void sualoaisp(ModelLoaisp modelloai) {
                showSua(modelloai);
            }

            @Override
            public void xoaloaisp(ModelLoaisp modelLoai) {
                showXoa(modelLoai);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        rcyvloaisp.setLayoutManager(linearLayoutManager);
        rcyvloaisp.setAdapter(productTypeAdapter);
    }

    private  void showXoa(ModelLoaisp loai){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa sản phẩm ");
        builder.setMessage("Bạn có muốn xóa sản phẩm ");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // xóa sản phẩm có mã loai liên kết với sản phẩm
                DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference("sanpham");
                dr1.orderByChild("maloai").equalTo(loai.getMaloai()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

                // xóa loại
                DatabaseReference databaseReferenceXoal = database.getReference("loaisp");
                databaseReferenceXoal.child(String.valueOf(loai.getMaloai())).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Xóa loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNeutralButton("Không ",null);
        builder.show();

    }
    private void showSua(ModelLoaisp modelloai){

        // hiện dinalog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dinalogsualoai);
        dialog.setCancelable(false);

        Button btSua = dialog.findViewById(R.id.btSua);
        Button btback = dialog.findViewById(R.id.btbackl);
        EditText etsua = dialog.findViewById(R.id.edtSual);
        ivSuaAnh = dialog.findViewById(R.id.ivSuaLoai);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("loaisp");

        ivSuaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooserL();
            }
        });

        // đưa tên loại từ list lên dinalog
        etsua.setText(modelloai.getTenloai());

        // sửa loại
        btSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = etsua.getText().toString().trim();
                modelloai.setTenloai(ten);

                if(ten.isEmpty()){
                    kiemtrasua();
                }else{
                    databaseReference1.child(String.valueOf(modelloai.getMaloai())).updateChildren(modelloai.toMap(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(getContext(), "Thành công ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    uploadFilel(modelloai);
                }
            }
        });
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void xoasp(ModelSanpham sp){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa sản phẩm ");
        builder.setMessage("Bạn có muốn xóa sản phẩm ");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseReferencexoa = database.getReference("sanpham");

                databaseReferencexoa.child(String.valueOf(sp.getMasp()-1)).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNeutralButton("Không ",null);
        builder.show();
    }

    private  void showSuasp(ModelSanpham sp){
        // hiện dinalog
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dinalog_suasp);
        dialog.setCancelable(false);

        Button btback = dialog.findViewById(R.id.btbacksp);
        Button btSua = dialog.findViewById(R.id.btSuasp);
        EditText edtTen = dialog.findViewById(R.id.edtTensuasp);
        EditText edtGia = dialog.findViewById(R.id.edtGiasuasp);
        Spinner spinner = dialog.findViewById(R.id.spinnersp);

        ivSuaAnh = dialog.findViewById(R.id.ivSuaAnh);

        ivSuaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // hiện spinner
        ArrayList<String> listsp = new ArrayList<>();
        DatabaseReference databaseReference1  = FirebaseDatabase.getInstance().getReference().child("loaisp");
        DatabaseReference databaseReferencesp  = FirebaseDatabase.getInstance().getReference().child("sanpham");

        ArrayAdapter<String> adaptersp = new ArrayAdapter<String>(getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listsp);
        spinner.setAdapter(adaptersp);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listsp.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name  = ds.child("tenloai").getValue(String.class);
                    listsp.add(name);
                }
                adaptersp.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        // đưa mẫu sẳn từ list lên dinalog
        edtTen.setText(sp.getTensp());
        edtGia.setText(sp.getGiasp());

        // sửa sản phẩm
        btSua.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString().trim();
                String gia = edtGia.getText().toString().trim();
                int maloai = spinner.getSelectedItemPosition();

                sp.setTensp(ten);
                sp.setGiasp(gia);
                sp.setMaloai(maloai);

                if(ten.isEmpty()|| gia.isEmpty()){
                    kiemtrasua();
                }else{
                    databaseReferencesp.child(String.valueOf(sp.getMasp())).updateChildren(sp.toMap(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(getContext(), "Thêm sản phẩm thành công  ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    uploadFilesp(sp);
                }
            }
        });

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void openFileChooserL() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            ivSuaAnh.setImageURI(mImageUri);
        }
    }

    // up lên storage
    private void uploadFilesp(ModelSanpham sp) {
        if (mImageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference fileReference = mStorageRef.child("sanpham/" + System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Thêm đường dẫn của ảnh vào Firebase Realtime Database
                            String path = String.valueOf(sp.getMasp());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sanpham").child(path);
                            databaseReference.child("anh").setValue(imageUrl);
                        });
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            Toast.makeText(requireContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFilel(ModelLoaisp loai) {
        if (mImageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference fileReference = mStorageRef.child("loaisp/" + System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Thêm đường dẫn của ảnh vào Firebase Realtime Database
                            String path = String.valueOf(loai.getMaloai());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("loaisp").child(path);
                            databaseReference.child("anh").setValue(imageUrl);
                        });
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            Toast.makeText(requireContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        if (getContext() == null) {
            return null;
        }
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }

    private void kiemtrasua(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông Báo ");
        builder.setMessage("Vui lòng nhập đầy đủ thông tin  ");
        builder.setNegativeButton("Ok",null);
        builder.show();
    }


}