package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.account.ProfileSetupActivity;
import com.example.hatti.adapter.ProductPriceListAdapter;
import com.example.hatti.internet.NetworkBroadcast;
import com.example.hatti.models.OrderModel;
import com.example.hatti.models.ProductListPriceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class OrderDetailActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView date,noOfProducts,price,status,methode;
    RecyclerView rvProductList;
    AppCompatButton cancelOrder;
    ArrayList<ProductListPriceModel> list = new ArrayList<>();
    TextView fullAmount,discount,totalAmount;
    int intTotalMrp = 0,intTotalAmount,adminOrderId;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    String strPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        date = findViewById(R.id.tvOrderDetailDate);
        noOfProducts = findViewById(R.id.tvOrderDetailNoOfProduct);
        price = findViewById(R.id.tvOrderDetailAmount);
        status = findViewById(R.id.tvOrderDetailStatus);
        methode = findViewById(R.id.tvOrderDetailPaymentMethode);
        rvProductList = findViewById(R.id.rvProductPrice);
        cancelOrder = findViewById(R.id.btnOrderDetailCancelOrder);
        fullAmount = findViewById(R.id.tvProductPriceTotalMrp);
        discount = findViewById(R.id.tvProductPriceTotalDiscount);
        totalAmount = findViewById(R.id.tvProductPriceTotalAmount);

        Intent intent = getIntent();
        strPrice = intent.getStringExtra("price");
        String strOrderId = intent.getStringExtra("orderId");

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (strPrice.equals("0")){
            database.getReference().child("Users").child(auth.getUid()).child("Order").child("miniOrder").child(strOrderId).child("price").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    strPrice = snapshot.getValue(String.class);
                    price.setText(strPrice);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
//        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        ProductPriceListAdapter adapter = new ProductPriceListAdapter(list,OrderDetailActivity.this);
        rvProductList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailActivity.this);
        rvProductList.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Order").child("fullOrder").child(strOrderId).child("detail")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                OrderModel detailModel = snapshot.getValue(OrderModel.class);
                                date.setText(Objects.requireNonNull(detailModel).getDate()+" "+detailModel.getTime());
                                noOfProducts.setText("("+detailModel.getNoOfProduct()+")");
                                price.setText("Rs. "+strPrice);
                                status.setText(detailModel.getStatus());
                                methode.setText(detailModel.getMethode());
                                totalAmount.setText(detailModel.getPrice());
                                adminOrderId = detailModel.getAdminOrderId();
                                intTotalAmount = Integer.parseInt(detailModel.getPrice());

                                if (Objects.equals(detailModel.getStatus(), "Cancel Order") || Objects.equals(detailModel.getStatus(), "Order Delever")){
                                    cancelOrder.setVisibility(View.GONE);
                                }
                                if (Objects.equals(detailModel.getStatus(), "Cancel Order")){
                                    status.setTextColor(Color.parseColor("#ff0000"));
                                } else if (Objects.equals(detailModel.getStatus(), "Order Delever")) {
                                    status.setTextColor(Color.parseColor("#689F38"));

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(OrderDetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
        database.getReference().child("Users").child(auth.getUid()).child("Order").child("fullOrder").child(strOrderId).child("list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            ProductListPriceModel Products = snapshot1.getValue(ProductListPriceModel.class);
                            list.add(Products);

                            int temMrp = Integer.parseInt(Objects.requireNonNull(Products).getMrp());
                            int temQty = Integer.parseInt(Products.getQty());
                            intTotalMrp += temMrp*temQty;
                        }
                        fullAmount.setText(intTotalMrp+"");

                        discount.setText((intTotalMrp-intTotalAmount)+"");
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OrderDetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        cancelOrder.setOnClickListener(view -> {
            database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Order").child("fullOrder").child(strOrderId).child("detail")
                    .child("status").setValue("Cancel Order");
            database.getReference().child("Hatti").child("All Order").child(adminOrderId+"").child("status").setValue("Cancel Order");
            database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Order").child("miniOrder").child(strOrderId).child("status").setValue("Cancel Order");
            database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Payment").child("payment list").child(strOrderId).removeValue();

            com.example.hatti.notification.FcmNotificationsSender notificationsSender = new com.example.hatti.notification.FcmNotificationsSender("/topics/admin","Order Cancel","Order Cancel by client ",getApplicationContext(), OrderDetailActivity.this);
            notificationsSender.SendNotifications();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.tolProfile){
            Intent intent = new Intent(OrderDetailActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(OrderDetailActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(OrderDetailActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}