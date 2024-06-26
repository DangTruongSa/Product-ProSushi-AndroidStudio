package com.example.prosushi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prosushi.Model.ModelLoaisp;
import com.example.prosushi.R;

import java.util.ArrayList;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.MyViewHolder>{

    Context context;
    ArrayList<ModelLoaisp> list;

    public SpinnerAdapter(Context context, ArrayList<ModelLoaisp> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oneitem_spinner, parent, false);
        return new SpinnerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelLoaisp modelLoaisp = list.get(position);
        holder.tvItemloai.setText(modelLoaisp.getTenloai());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvItemloai;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemloai =(TextView) itemView.findViewById(R.id.tenloaiitem);

        }
    }
}
