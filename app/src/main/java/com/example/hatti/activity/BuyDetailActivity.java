package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.adapter.ProductPriceListAdapter;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.ProductListPriceModel;
import com.example.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyDetailActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView rvProductPrice;
    ArrayList<ProductListPriceModel> productList = new ArrayList<>();
    TextView tvTotalPrice,tvTotalMrp,tvTotalDiscount;
    int totalAmount,totalMrp=0,totalDiscount=0,noOfProducts=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);

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



//        Intent intent = getIntent();
//        totalAmount = intent.getIntExtra("Amount",0);
//        Toast.makeText(this, ""+totalAmount, Toast.LENGTH_SHORT).show();
////        productList = intent.getStringArrayListExtra("list");

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(BuyDetailActivity.this,BuyConformActivity.class);
                intent.putExtra("price",totalAmount+"");
                intent.putExtra("noOfProduct",noOfProducts+"");
                startActivity(intent);
                finish();
            }
        });
    }
}