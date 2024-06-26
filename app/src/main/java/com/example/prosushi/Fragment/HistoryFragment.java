package com.example.prosushi.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prosushi.Adapter.HistoryAdapter;
import com.example.prosushi.Model.ModelHoadon;
import com.example.prosushi.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    HistoryAdapter historyAdapter;

    RecyclerView recyclerView;

    ArrayList<ModelHoadon> listhd;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        SharedPreferences getshare = getContext().getSharedPreferences("role", Context.MODE_PRIVATE);
        int role = getshare.getInt("rolekey",0);
        int smand = getshare.getInt("mand",0);

        recyclerView=view.findViewById(R.id.rcyvhoadon);
        database= FirebaseDatabase.getInstance();
        reference = database.getReference("hoadon");

        listhd = new ArrayList<>();

        historyAdapter = new HistoryAdapter(getContext(),listhd);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(historyAdapter);

        switch (role){
            case 1:
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ModelHoadon hd = snapshot.getValue(ModelHoadon.class);
                        listhd.add(hd);
                        historyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        ModelHoadon hd = snapshot.getValue(ModelHoadon.class);
                        if (hd == null || listhd == null || listhd.isEmpty()) {
                            return;
                        }
                        for (int i = 0; i < listhd.size(); i++) {
                            if (hd.getMahd() == listhd.get(i).getMahd()) {
                                listhd.set(i, hd);
                            }
                        }
                        historyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case 2:
                reference.orderByChild("mand").equalTo(smand).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ModelHoadon hd = snapshot.getValue(ModelHoadon.class);
                        listhd.add(hd);
                        historyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        ModelHoadon hd = snapshot.getValue(ModelHoadon.class);
                        if (hd == null || listhd == null || listhd.isEmpty()) {
                            return;
                        }
                        for (int i = 0; i < listhd.size(); i++) {
                            if (hd.getMahd() == listhd.get(i).getMahd()) {
                                listhd.set(i, hd);
                            }
                        }
                        historyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }







        return view;
    }

    public void loaddatahoadon(){


        //add arraylist voi firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelHoadon hoadon = dataSnapshot.getValue(ModelHoadon.class);
                    listhd.add(hoadon);
                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loaddatarecycleview(){

    }
}