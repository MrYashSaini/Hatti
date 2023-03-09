package com.echatti.hatti.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.echatti.hatti.R;
import com.echatti.hatti.adapter.OrderAdapter;
import com.echatti.hatti.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class OrderFragment extends Fragment {
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<OrderModel> list = new ArrayList<>();
    RecyclerView orderRecyclerView;
    LinearLayout backgroundLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        orderRecyclerView = view.findViewById(R.id.rvOrderList);
        progressBar = view.findViewById(R.id.pbOrderProgressBar);
        backgroundLayout = view.findViewById(R.id.llOrderBackgroundLayout);
        OrderAdapter adapter = new OrderAdapter(getContext(),list);
        orderRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        orderRecyclerView.setLayoutManager(linearLayoutManager);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu);

        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Order").child("miniOrder")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if (snapshot.getChildrenCount()!=0){
                            progressBar.setVisibility(View.VISIBLE);
                        }else {
                            backgroundLayout.setVisibility(View.VISIBLE);
                        }
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            OrderModel model = snapshot1.getValue(OrderModel.class);
                            list.add(model);
                        }
                        Collections.reverse(list);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        orderRecyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}