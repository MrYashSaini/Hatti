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
import com.example.hatti.adapter.CategoryFragmentAdapter;
import com.example.hatti.models.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<CategoryModel> list;
    private CategoryFragmentAdapter adapter;



    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.rvCategoryFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

//        List<String> main_category = new ArrayList<>();
        List<String> nestedList1 = new ArrayList<>();
        List<String> nestedList2 = new ArrayList<>();
        List<String> nestedList3 = new ArrayList<>();
        List<String> nestedList4 = new ArrayList<>();

//        nestedList1.add("hello");

        database.getReference().child("categorys").child("name").child("category").child("f1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            nestedList1.add(snapshot1.getValue(String.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "error :=>"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        database.getReference().child("categorys").child("name").child("category").child("f2")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            nestedList2.add(snapshot1.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        database.getReference().child("categorys").child("name").child("category").child("f3")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            nestedList3.add(snapshot1.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        database.getReference().child("categorys").child("name").child("category").child("f4")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            nestedList4.add(snapshot1.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        list.add(new CategoryModel(nestedList1, "Snacks"));
        list.add(new CategoryModel(nestedList2,"Hygiene"));
        list.add(new CategoryModel(nestedList3,"Beverages"));
        list.add(new CategoryModel(nestedList4,"Cleaning"));

        adapter = new CategoryFragmentAdapter(list,getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }


}