package com.example.prosushi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.prosushi.Model.ModelCTHD;
import com.example.prosushi.Model.ModelSanpham;
import com.example.prosushi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CTHDAdapter extends BaseAdapter {
Context c;
ArrayList<ModelCTHD> listct;

ArrayList<ModelSanpham> listsp;
String mangsp[]= new String[]{"","Sushi Tuna", "Sushi Octopus", "Sushi Alligator", "Sushi Squid", "Wasabi", "BlackPepper", "Katana", "Fillet Knife", "Ladle", "Pot"};
    public CTHDAdapter(Context c, ArrayList<ModelCTHD> listct) {
        this.c = c;
        this.listct = listct;
    }

    @Override
    public int getCount() {
        return listct.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)c).getLayoutInflater();
        convertView = inflater.inflate(R.layout.oneitem_cthd,parent,false);

        TextView tvtenspct = (TextView) convertView.findViewById(R.id.tvtenspcthd);
        TextView tvslct = (TextView) convertView.findViewById(R.id.tvslcthd);
        TextView tvthanhtienct = (TextView) convertView.findViewById(R.id.tvgiacthd);


        tvtenspct.setText(String.valueOf(mangsp[listct.get(position).getMasp()]));
        tvslct.setText(String.valueOf(listct.get(position).getSoluong()));
        tvthanhtienct.setText(String.valueOf(listct.get(position).getThanhtien()));


        return convertView;
    }


}
