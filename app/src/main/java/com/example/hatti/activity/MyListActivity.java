package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.adapter.CartAdapter;
import com.example.hatti.adapter.MyListAdapter;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.categoryProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyListActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView rvProductShow;
    ArrayList<categoryProductModel> productList = new ArrayList<>();
    TextView removeAll,addAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rvProductShow = findViewById(R.id.rvMylistCategory);
        addAll = findViewById(R.id.tvMyListAddAll);
        removeAll = findViewById(R.id.tvMyListRemoveAll);
        MyListAdapter adapter = new MyListAdapter(productList,MyListActivity.this);
        rvProductShow.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyListActivity.this);
        rvProductShow.setLayoutManager(linearLayoutManager);

        addAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Users").child(auth.getUid()).child("My List").child("list")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    CartModel products = dataSnapshot.getValue(CartModel.class);
                                    products.setQty(1);
                                    database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(products.getCategory()+products.getProductId())
                                            .setValue(products)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task){
                                                }
                                            });
                                }
                                Toast.makeText(MyListActivity.this,"Add On Cart", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MyListActivity.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        database.getReference().child("Users").child(auth.getUid()).child("My List").child("list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            CartModel products = dataSnapshot.getValue(CartModel.class);
                            database.getReference().child("categorys").child("product category").child(products.getCategory()).child(products.getProductId())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot){
                                            categoryProductModel productDetails = snapshot.getValue(categoryProductModel.class);
                                            categoryProductModel full = new categoryProductModel();
                                            full.setImage(productDetails.getImage());
                                            full.setName(productDetails.getName());
                                            full.setPrice(productDetails.getPrice());
                                            full.setMrp(productDetails.getMrp());
                                            full.setProductId(productDetails.getProductId());
                                            full.setCategory(productDetails.getCategory());
                                            full.setDescription(productDetails.getDescription());
                                            productList.add(full);
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
//                                            Toast.makeText(getContext(), "not get data from product category", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getContext(), "not get data from cart", Toast.LENGTH_SHORT).show();
                    }
                });
//        Toast.makeText(getContext(), "list size"+productList.size(), Toast.LENGTH_SHORT).show();

        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Users").child(auth.getUid()).child("My List").child("list").removeValue();
                Toast.makeText(MyListActivity.this, "remove all", Toast.LENGTH_SHORT).show();
            }
        });

    }
}