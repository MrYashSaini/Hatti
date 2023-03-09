package com.echatti.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echatti.hatti.MainActivity;
import com.echatti.hatti.R;
import com.echatti.hatti.account.ProfileSetupActivity;
import com.echatti.hatti.internet.NetworkBroadcast;
import com.echatti.hatti.models.AllOrderModel;
import com.echatti.hatti.models.CartModel;
import com.echatti.hatti.models.OrderModel;
import com.echatti.hatti.models.PaymentModel;
import com.echatti.hatti.models.ProfileModel;
import com.echatti.hatti.models.categoryProductModel;
import com.echatti.hatti.models.productListModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class BuyConformActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private ArrayList<categoryProductModel> productList = new ArrayList<>();
    private int clientOrderId;
    private int adminOrderId;
    private int updateAdminOrderId;
    ProgressBar progressBar;
    Toolbar toolbar;
    ConstraintLayout linearLayout;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_conform);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        TextView price = findViewById(R.id.tvBuyConformPrice);
        AppCompatButton exit = findViewById(R.id.btnBuyConformExit);
        database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.pbBuyConformProgressBar);
        linearLayout= findViewById(R.id.llBuyConformActivityLayout);
        //        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Intent intent = getIntent();
        String no_of_product = intent.getStringExtra("noOfProduct");
        String totalprice =  intent.getStringExtra("price");
        String authId = intent.getStringExtra("userId");
        clientOrderId = intent.getIntExtra("clientOrderId",0);
        adminOrderId  = intent.getIntExtra("adminOrderId",0);
        updateAdminOrderId = adminOrderId+1;
        int updateClientOrderId = clientOrderId + 1;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        String strDate = formatter.format(date);

        DateFormat time = new SimpleDateFormat("hh:mm aa");
        String StrTime = time.format(new Date());

        OrderModel model = new OrderModel();
        model.setPrice(totalprice);
        model.setNoOfProduct(no_of_product);
        model.setStatus("Order Placed");
        model.setDate(strDate);
        model.setTime(StrTime+"");
        model.setOrderId(clientOrderId);

        price.setText(totalprice);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        exit.setOnClickListener(v -> {
            startActivity(new Intent(BuyConformActivity.this, MainActivity.class));
            finish();
        });

//          update client order id
        database.getReference().child("Profiles").child(authId).child("orderId").setValue(updateClientOrderId);
//          update admin order id and add order in admin all order
        AllOrderModel model1 = new AllOrderModel();
        database.getReference().child("Profiles").child(authId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel model2 = snapshot.getValue(ProfileModel.class);
                model1.setId(authId);
                model1.setDate(strDate);
                model1.setStatus("Order Placed");
                model1.setImage(Objects.requireNonNull(model2).getProfilePhoto());
                model1.setPrice(totalprice);
                model1.setNoOfProduct(no_of_product);
                model1.setMethode("COD");
                model1.setTime(StrTime);
                model1.setName(model2.getName());
                model1.setOrderId(clientOrderId);
                model1.setAdminOrderId(adminOrderId);
                model1.setSeen(false);
                database.getReference().child("Hatti").child("id").child("orderId").setValue(updateAdminOrderId)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                database.getReference().child("Hatti").child("All Order").child(adminOrderId+"").setValue(model1);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyConformActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        add new order number
        database.getReference().child("Hatti").child("no order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int noOfOrder = snapshot.getValue(int.class);
                int update = noOfOrder+1;
                database.getReference().child("Hatti").child("no order").setValue(update);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyConformActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // add order
        database.getReference().child("Users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("Cart").child("list")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            CartModel products = dataSnapshot.getValue(CartModel.class);
                            assert products != null;
                            database.getReference().child("categorys").child("product category").child(products.getCategory()).child(products.getProductId())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot){
                                            categoryProductModel productDetails = snapshot.getValue(categoryProductModel.class);
                                            productListModel full = new productListModel();
                                            assert productDetails != null;
                                            full.setImage(productDetails.getImage());
                                            full.setName(productDetails.getName());
                                            full.setPrice(productDetails.getPrice());
                                            full.setMrp(productDetails.getMrp());
                                            full.setQty(products.getQty()+"");

                                            int price = Integer.parseInt(full.getPrice());
                                            int totalAmount = price*products.getQty();
                                            full.setTotalPrice(totalAmount+"");

                                            // basic detail
                                            OrderModel detailModel = new OrderModel();
                                            detailModel.setDate(strDate);
                                            detailModel.setTime(StrTime);
                                            detailModel.setMethode("COD");
                                            detailModel.setPrice(totalprice);
                                            detailModel.setNoOfProduct(no_of_product);
                                            detailModel.setStatus("Order Place");
                                            detailModel.setOrderId(clientOrderId);
                                            detailModel.setAdminOrderId(adminOrderId);
                                            database.getReference().child("Users").child(authId).child("Order").child("fullOrder").child(clientOrderId+"").child("detail").setValue(detailModel);
                                            database.getReference().child("Users").child(authId).child("Order").child("fullOrder").child(clientOrderId+"").child("list").child(productDetails.getCategory()+productDetails.getProductId()).setValue(full);
                                            //        add mini order
                                            database.getReference().child("Users").child(authId).child("Order").child("miniOrder").child(clientOrderId+"").setValue(model);

//                                          add payment
                                            PaymentModel paymentModel = new PaymentModel();
                                            paymentModel.setAmount(totalprice);
                                            paymentModel.setNoOfProduct(no_of_product);
                                            paymentModel.setDate(strDate);
                                            paymentModel.setPay("0");
                                            paymentModel.setTime(StrTime);
                                            paymentModel.setOrderId(clientOrderId);
                                            database.getReference().child("Users").child(authId).child("Payment").child("payment list").child(clientOrderId+"").setValue(paymentModel);
                                            database.getReference().child("Users").child(authId).child("Cart").child("list").child(products.getCategory()+products.getProductId()).removeValue();


                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(BuyConformActivity.this, "not get data from product category", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        com.echatti.hatti.notification.FcmNotificationsSender notificationsSender = new com.echatti.hatti.notification.FcmNotificationsSender("/topics/admin","Order","New Order Place by client "+model1.getName(),getApplicationContext(), BuyConformActivity.this);
                        notificationsSender.SendNotifications();
                        linearLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BuyConformActivity.this, "not get data from cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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
            Intent intent = new Intent(BuyConformActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(BuyConformActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(BuyConformActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(BuyConformActivity.this,MainActivity.class));
        finish();

    }
}