package com.example.hatti.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.adapter.NotificationAdapter;
import com.example.hatti.models.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationFragment extends Fragment {
    FirebaseDatabase database;
    FirebaseAuth auth;
    RecyclerView rvNotification;
    ArrayList<NotificationModel> list=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rvNotification = view.findViewById(R.id.rvNotificationList);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        String strDate = formatter.format(date);

        DateFormat time = new SimpleDateFormat("hh:mm aa");
        String StrTime = time.format(new Date()).toString();

        NotificationAdapter adapter = new NotificationAdapter(getContext(),list);
        rvNotification.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvNotification.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users").child(auth.getUid()).child("Notification").child("mini notification")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            NotificationModel model = snapshot1.getValue(NotificationModel.class);
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}