package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hatti.R;
import com.example.hatti.adapter.ProductPriceListAdapter;
import com.example.hatti.models.OrderModel;
import com.example.hatti.models.ProductListPriceModel;
import com.example.hatti.models.productListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView date,noOfProducts,price,status,methode;
    RecyclerView rvProductList;
    AppCompatButton cancelOrder;
    ArrayList<ProductListPriceModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        date = findViewById(R.id.tvOrderDetailDate);
        noOfProducts = findViewById(R.id.tvOrderDetailNoOfProduct);
        price = findViewById(R.id.tvOrderDetailAmount);
        status = findViewById(R.id.tvOrderDetailStatus);
        methode = findViewById(R.id.tvOrderDetailPaymentMethode);
        rvProductList = findViewById(R.id.rvProductPrice);
        cancelOrder = findViewById(R.id.btnOrderDetailCancelOrder);

        Intent intent = getIntent();
        String strDate = intent.getStringExtra("date");
        String StrTime = intent.getStringExtra("time");

        ProductPriceListAdapter adapter = new ProductPriceListAdapter(list,OrderDetailActivity.this);
        rvProductList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailActivity.this);
        rvProductList.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users").child(auth.getUid()).child("Order").child("fullOrder").child(strDate+" "+StrTime).child("detail")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                OrderModel detailModel = snapshot.getValue(OrderModel.class);
                                date.setText(detailModel.getDate()+" "+detailModel.getTime());
                                noOfProducts.setText(detailModel.getNoOfProduct());
                                price.setText(detailModel.getPrice());
                                status.setText(detailModel.getStatus());
                                methode.setText(detailModel.getMethode());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        database.getReference().child("Users").child(auth.getUid()).child("Order").child("fullOrder").child(strDate+" "+StrTime).child("list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            ProductListPriceModel Products = snapshot1.getValue(ProductListPriceModel.class);
                            list.add(Products);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}