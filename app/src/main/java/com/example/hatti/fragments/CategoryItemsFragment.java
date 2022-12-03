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
import com.example.hatti.adapter.CategoryProductAdapter;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.HorizontalProductScrollModel;
import com.example.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CategoryItemsFragment extends Fragment {
    FirebaseDatabase database;
    ArrayList<categoryProductModel> list =new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_items, container, false);
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.rvCategoryItemShow);

//        set adapter
        CategoryProductAdapter adapter = new CategoryProductAdapter(list,getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseDatabase finalDatabase = database;

        database.getReference().child("Users").child(auth.getUid()).child("CategoryShow").child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String  category = snapshot.getValue(String.class);
                if (category == "recommended"){
                    database.getReference().child("Hatti").child("recommended products").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                CartModel product = dataSnapshot.getValue(CartModel.class);
                                database.getReference().child("categorys").child("product category").child(product.getCategory()).child(product.getProductId())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                categoryProductModel products = snapshot.getValue(categoryProductModel.class);
                                                list.add(products);
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    finalDatabase.getReference().child("categorys").child("product category").child(category).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                categoryProductModel products = dataSnapshot.getValue(categoryProductModel.class);
                                list.add(products);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Product Not found", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}