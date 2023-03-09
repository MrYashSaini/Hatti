package com.echatti.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.echatti.hatti.R;
import com.echatti.hatti.account.ProfileSetupActivity;
import com.echatti.hatti.adapter.ProductPriceListAdapter;
import com.echatti.hatti.internet.NetworkBroadcast;
import com.echatti.hatti.models.CartModel;
import com.echatti.hatti.models.ProductListPriceModel;
import com.echatti.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyDetailActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private RecyclerView rvProductPrice;
    ArrayList<ProductListPriceModel> productList = new ArrayList<>();
    TextView tvTotalPrice,tvTotalMrp,tvTotalDiscount,call;
    private int totalAmount,totalMrp=0,totalDiscount=0,noOfProducts=0;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        AppCompatButton buy = findViewById(R.id.btnBuyDetailBuy);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        rvProductPrice = findViewById(R.id.rvProductPrice);
        ProductPriceListAdapter adapter1 = new ProductPriceListAdapter(productList,BuyDetailActivity.this);
        rvProductPrice.setAdapter(adapter1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(BuyDetailActivity.this);
        rvProductPrice.setLayoutManager(linearLayoutManager1);
        tvTotalPrice   = findViewById(R.id.tvProductPriceTotalAmount);
        tvTotalMrp     = findViewById(R.id.tvProductPriceTotalMrp);
        tvTotalDiscount= findViewById(R.id.tvProductPriceTotalDiscount);
        call = findViewById(R.id.btnBuyDetailCallButton);


        //        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

//      Product Price List
        database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list")
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
                                            ProductListPriceModel itemList = new ProductListPriceModel();
                                            itemList.setImage(productDetails.getImage());
                                            itemList.setName(productDetails.getName());
                                            itemList.setPrice(productDetails.getPrice());
                                            itemList.setMrp(productDetails.getMrp());
//                                            itemList.setProductId(productDetails.getProductId());
//                                            itemList.setCategory(productDetails.getCategory());
                                            itemList.setQty(products.getQty()+"");


                                            Integer amount = Integer.valueOf(itemList.getPrice());
                                            int price = amount.intValue();
                                            int total = (price * products.getQty());
                                            totalAmount +=total;
                                            itemList.setTotalPrice(total+"");
                                            productList.add(itemList);
                                            Integer mrpAmount = Integer.valueOf(itemList.getMrp());
                                            int mrp = mrpAmount.intValue();
                                            totalMrp = totalMrp+(mrp*products.getQty());

                                            noOfProducts += 1;
                                            totalDiscount = totalDiscount+((mrp*products.getQty())-(price*products.getQty()));
                                            tvTotalDiscount.setText(totalDiscount+"");
                                            tvTotalPrice.setText(totalAmount+"");
                                            tvTotalMrp.setText(totalMrp+"");
                                            adapter1.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(BuyDetailActivity.this, "not get data from product category", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BuyDetailActivity.this, "not get data from cart", Toast.LENGTH_SHORT).show();
                    }
                });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Profiles").child(auth.getCurrentUser().getUid()).child("orderId")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int clientOrderId = snapshot.getValue(int.class);
                                database.getReference().child("Hatti").child("id").child("orderId")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int adminOrderId = snapshot.getValue(int.class);
                                                Intent intent =new Intent(BuyDetailActivity.this,BuyConformActivity.class);
                                                intent.putExtra("price",totalAmount+"");
                                                intent.putExtra("noOfProduct",noOfProducts+"");
                                                intent.putExtra("userId",auth.getCurrentUser().getUid());
                                                intent.putExtra("clientOrderId",clientOrderId);
                                                intent.putExtra("adminOrderId",adminOrderId);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(BuyDetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(BuyDetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(BuyDetailActivity.this);
                dialog.setContentView(R.layout.call_dialog_box);
                dialog.show();
                Button button = dialog.findViewById(R.id.btnCallDialogBoxButton);
                TextView msg = dialog.findViewById(R.id.tvCallDialogBoxMsg);
                msg.setText("Order Place by Call");
                button.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    database.getReference().child("Hatti").child("contact detail").child("phoneNo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String phoneNumber=snapshot.getValue(String.class);
                            if (Build.VERSION.SDK_INT > 22) {

                                if (ActivityCompat.checkSelfPermission(BuyDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions((Activity) BuyDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                                    return;
                                }
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phoneNumber.trim()));
                                startActivity(callIntent);
                            } else {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phoneNumber.trim()));
                                startActivity(callIntent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BuyDetailActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                });

            }
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
            Intent intent = new Intent(BuyDetailActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(BuyDetailActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(BuyDetailActivity.this, AboutActivity.class);
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