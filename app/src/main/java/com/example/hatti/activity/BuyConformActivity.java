package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.MainActivity;
import com.example.hatti.R;

import com.example.hatti.models.CartModel;
import com.example.hatti.models.OrderModel;
import com.example.hatti.models.PaymentModel;
import com.example.hatti.models.categoryProductModel;
import com.example.hatti.models.productListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BuyConformActivity extends AppCompatActivity {
    TextView price;
    AppCompatButton exit;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<categoryProductModel> productList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_conform);
        price = findViewById(R.id.tvBuyConformPrice);
        exit = findViewById(R.id.btnBuyConformExit);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String no_of_product = intent.getStringExtra("noOfProduct");
        String totalprice =  intent.getStringExtra("price");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        String strDate = formatter.format(date);

        DateFormat time = new SimpleDateFormat("hh:mm aa");
        String StrTime = time.format(new Date()).toString();

        Toast.makeText(this, "d="+strDate+"\n"+StrTime, Toast.LENGTH_SHORT).show();
        OrderModel model = new OrderModel();
        model.setPrice(totalprice);
        model.setNoOfProduct(no_of_product);
        model.setStatus("In Root");
        model.setDate(strDate);
        model.setTime(StrTime+"");


        database.getReference().child("Users").child(auth.getUid()).child("Order").child("miniOrder").child(strDate+" "+StrTime).setValue(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(BuyConformActivity.this,"order place",Toast.LENGTH_SHORT).show();
                            }
                        });


        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setAmount(totalprice);
        paymentModel.setNoOfProduct(no_of_product);
        paymentModel.setDate(strDate);
        paymentModel.setPay("0");
        paymentModel.setTime(StrTime);
        database.getReference().child("Users").child(auth.getUid()).child("Payment").child("payment list").child(strDate+" "+StrTime).setValue(paymentModel);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyConformActivity.this, MainActivity.class));
            }
        });
        price.setText(totalprice);


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
                                            productListModel full = new productListModel();
                                            full.setImage(productDetails.getImage());
                                            full.setName(productDetails.getName());
                                            full.setPrice(productDetails.getPrice());
                                            full.setMrp(productDetails.getMrp());
                                            full.setQty(products.getQty()+"");

                                            Integer amount = Integer.valueOf(full.getPrice());
                                            int price = amount.intValue();
                                            int totalAmount = price*products.getQty();
                                            full.setTotalPrice(totalAmount+"");

                                            // basic detail
                                            OrderModel detailModel = new OrderModel();
                                            detailModel.setDate(strDate);
                                            detailModel.setTime(StrTime);
                                            detailModel.setMethode("COD");
                                            detailModel.setPrice(productDetails.getPrice());
                                            detailModel.setNoOfProduct(no_of_product);
                                            detailModel.setStatus("In Root");
                                            database.getReference().child("Users").child(auth.getUid()).child("Order").child("fullOrder").child(strDate+" "+StrTime).child("detail").setValue(detailModel);
                                            database.getReference().child("Users").child(auth.getUid()).child("Order").child("fullOrder").child(strDate+" "+StrTime).child("list").child(productDetails.getCategory()+productDetails.getProductId()).setValue(full);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(BuyConformActivity.this, "not get data from product category", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BuyConformActivity.this, "not get data from cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}