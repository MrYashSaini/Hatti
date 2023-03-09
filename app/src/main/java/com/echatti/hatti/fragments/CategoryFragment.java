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
import com.echatti.hatti.adapter.CategoryFragmentAdapter;
import com.echatti.hatti.models.CategoryModel;
import com.echatti.hatti.models.MainCategoryModel;
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
    MainCategoryModel model;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        progressBar = view.findViewById(R.id.pbCategoryProgressBar);
        linearLayout = view.findViewById(R.id.llCategoryFragmentLayout);
        recyclerView = view.findViewById(R.id.rvCategoryFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        // Set the drawable icon to the Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu);

        List<String> nestedList1 = new ArrayList<>();
        List<String> nestedList2 = new ArrayList<>();
        List<String> nestedList3 = new ArrayList<>();
        List<String> nestedList4 = new ArrayList<>();
        List<String> nestedList5 = new ArrayList<>();
        List<String> nestedList6 = new ArrayList<>();
        List<String> nestedList7 = new ArrayList<>();
        List<String> nestedList8 = new ArrayList<>();
        List<String> nestedList9 = new ArrayList<>();
        List<String> nestedList10 = new ArrayList<>();
        List<String> nestedList11= new ArrayList<>();

        database.getReference().child("categorys").child("name").child("main")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        model = snapshot.getValue(MainCategoryModel.class);
                        database.getReference().child("categorys").child("name").child("category").child("a")
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
                        database.getReference().child("categorys").child("name").child("category").child("b")
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
                        database.getReference().child("categorys").child("name").child("category").child("c")
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
                        database.getReference().child("categorys").child("name").child("category").child("d")
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
                        database.getReference().child("categorys").child("name").child("category").child("e")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            nestedList5.add(snapshot1.getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        database.getReference().child("categorys").child("name").child("category").child("f")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            nestedList6.add(snapshot1.getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        database.getReference().child("categorys").child("name").child("category").child("g")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            nestedList7.add(snapshot1.getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        database.getReference().child("categorys").child("name").child("category").child("h")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            nestedList8.add(snapshot1.getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        database.getReference().child("categorys").child("name").child("category").child("i")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            nestedList9.add(snapshot1.getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        database.getReference().child("categorys").child("name").child("category").child("j")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            nestedList10.add(snapshot1.getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        database.getReference().child("categorys").child("name").child("category").child("k")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            nestedList11.add(snapshot1.getValue(String.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                        list.add(new CategoryModel(nestedList1, model.getA()));
                        list.add(new CategoryModel(nestedList2,model.getB()));
                        list.add(new CategoryModel(nestedList3,model.getC()));
                        list.add(new CategoryModel(nestedList4,model.getD()));
                        list.add(new CategoryModel(nestedList5,model.getE()));
                        list.add(new CategoryModel(nestedList6,model.getF()));
                        list.add(new CategoryModel(nestedList7,model.getG()));
                        list.add(new CategoryModel(nestedList8,model.getH()));
                        list.add(new CategoryModel(nestedList9,model.getI()));
                        list.add(new CategoryModel(nestedList10,model.getJ()));
                        list.add(new CategoryModel(nestedList11,model.getK()));

                        adapter = new CategoryFragmentAdapter(list,getContext());
                        recyclerView.setAdapter(adapter);
                        linearLayout.setVisibility(view.VISIBLE);
                        progressBar.setVisibility(view.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }


}