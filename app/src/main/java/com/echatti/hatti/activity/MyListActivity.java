package com.echatti.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echatti.hatti.R;
import com.echatti.hatti.account.ProfileSetupActivity;
import com.echatti.hatti.adapter.MyListAdapter;
import com.echatti.hatti.internet.NetworkBroadcast;
import com.echatti.hatti.models.CartModel;
import com.echatti.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class MyListActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView rvProductShow;
    ArrayList<categoryProductModel> productList = new ArrayList<>();
    TextView removeAll,addAll;
    ProgressBar progressBar;
    LinearLayout linearLayout,backgroundLayout;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rvProductShow = findViewById(R.id.rvMylistCategory);
        addAll = findViewById(R.id.tvMyListAddAll);
        removeAll = findViewById(R.id.tvMyListRemoveAll);
        progressBar = findViewById(R.id.pbMyListProgressBar);
        linearLayout = findViewById(R.id.llMyListActivityLayout);
        backgroundLayout = findViewById(R.id.llMyListBackgroundLayout);

        //        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        MyListAdapter adapter = new MyListAdapter(productList,MyListActivity.this);
        rvProductShow.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyListActivity.this);
        rvProductShow.setLayoutManager(linearLayoutManager);

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        addAll.setOnClickListener(v -> {
            if (productList.size()!=0){
                database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("My List").child("list")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    CartModel products = dataSnapshot.getValue(CartModel.class);
                                    Objects.requireNonNull(products).setQty(1);
                                    database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(products.getCategory()+products.getProductId())
                                            .setValue(products)
                                            .addOnCompleteListener(task -> {
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
            else {
                Toast.makeText(MyListActivity.this, "select product", Toast.LENGTH_SHORT).show();
            }
        });

        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("My List").child("list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        if (snapshot.getChildrenCount()!=0){
                            progressBar.setVisibility(View.VISIBLE);
                        }else {
                            backgroundLayout.setVisibility(View.VISIBLE);
                        }
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            CartModel products = dataSnapshot.getValue(CartModel.class);
                            database.getReference().child("categorys").child("product category").child(Objects.requireNonNull(products).getCategory()).child(products.getProductId())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot){
                                            categoryProductModel productDetails = snapshot.getValue(categoryProductModel.class);
                                            categoryProductModel full = new categoryProductModel();
                                            full.setImage(Objects.requireNonNull(productDetails).getImage());
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
                                                Toast.makeText(MyListActivity.this, ""+error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        adapter.notifyDataSetChanged();
                        linearLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyListActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        removeAll.setOnClickListener(v -> {
            database.getReference().child("Users").child(auth.getUid()).child("My List").child("list").removeValue();
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
            Intent intent = new Intent(MyListActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(MyListActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(MyListActivity.this, AboutActivity.class);
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