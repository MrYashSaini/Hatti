package com.echatti.hatti.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echatti.hatti.R;
import com.echatti.hatti.activity.BuyDetailActivity;
import com.echatti.hatti.adapter.CartAdapter;
import com.echatti.hatti.models.CartModel;
import com.echatti.hatti.models.ProductListPriceModel;
import com.echatti.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView rvProductShow,rvProductPrice;
    ArrayList<categoryProductModel> productList = new ArrayList<>();
    ArrayList<ProductListPriceModel> productPriceList = new ArrayList<>();
    TextView call,buy,removeAll,tvTotalPrice,tvTotalMrp,tvTotalDiscount,selectProduct,refresh;
    int totalAmount= 0,totalMrp=0,totalDiscount=0;
    ProgressBar progressBar;
    LinearLayout linearLayout,backgroundLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        auth           = FirebaseAuth.getInstance();
        database       = FirebaseDatabase.getInstance();
        rvProductShow  = view.findViewById(R.id.rvCartProductShow);
        removeAll      = view.findViewById(R.id.tvCartRemoveAll);
        buy            = view.findViewById(R.id.tvCartBuy);
        rvProductPrice = view.findViewById(R.id.rvProductPrice);
        tvTotalPrice   = view.findViewById(R.id.tvProductPriceTotalAmount);
        tvTotalMrp     = view.findViewById(R.id.tvProductPriceTotalMrp);
        tvTotalDiscount= view.findViewById(R.id.tvProductPriceTotalDiscount);
        call           = view.findViewById(R.id.tvCartCall);
        refresh         = view.findViewById(R.id.tvCartRefresh);
        progressBar = view.findViewById(R.id.pbCartProgressBar);
        selectProduct = view.findViewById(R.id.tvCartSelectProduct);
        linearLayout = view.findViewById(R.id.llCartFragmentLayout);
        backgroundLayout = view.findViewById(R.id.llCartBackgroundLayout);
//      show product in Recycler View
        CartAdapter adapter = new CartAdapter(productList,getContext(),rvProductShow,refresh);
        rvProductShow.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvProductShow.setLayoutManager(linearLayoutManager);

//      buy product
        buy.setOnClickListener(v -> {
            if (productList.size()!=0){
                Intent intent = new Intent(getContext(),BuyDetailActivity.class);
                intent.putExtra("Amount",98);
                intent.putExtra("discount",totalDiscount);
                intent.putExtra("mrp",totalMrp);
                startActivity(intent);
            }
            else {
                Toast.makeText(getContext(), "select product", Toast.LENGTH_SHORT).show();
            }
            
        });
//      show all product list with price
//        ProductPriceListAdapter adapter1 = new ProductPriceListAdapter(productPriceList,getContext());
//        rvProductPrice.setAdapter(adapter1);
//        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
//        rvProductPrice.setLayoutManager(linearLayoutManager1);
        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Cart").child("list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        if (snapshot.getChildrenCount()!=0){
                            progressBar.setVisibility(View.VISIBLE);
                        }else {
                            backgroundLayout.setVisibility(View.VISIBLE);
                            removeAll.setVisibility(View.GONE);
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
                                            full.setQty(products.getQty());
                                            productList.add(full);

//                                            ProductListPriceModel itemList = new ProductListPriceModel();
//                                            itemList.setImage(productDetails.getImage());
//                                            itemList.setName(productDetails.getName());
//                                            itemList.setPrice(productDetails.getPrice());
//                                            itemList.setMrp(productDetails.getMrp());
//                                            itemList.setQty(products.getQty()+"");
//
//                                            int price = Integer.parseInt(itemList.getPrice());
//                                            int total = (price * products.getQty());
//                                            totalAmount +=total;
//                                            itemList.setTotalPrice(total+"");
//                                            productPriceList.add(itemList);
//
//                                            int mrp = Integer.parseInt(full.getMrp());
//                                            totalMrp = totalMrp+(mrp*products.getQty());
//
//                                            totalDiscount = totalDiscount+((mrp*products.getQty())-(price*products.getQty()));
//                                            tvTotalDiscount.setText(totalDiscount+"");
//                                            tvTotalPrice.setText(totalAmount+"");
//                                            tvTotalMrp.setText(totalMrp+"");
                                            adapter.notifyDataSetChanged();
//                                            adapter1.notifyDataSetChanged();
                                            linearLayout.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "not get data from cart", Toast.LENGTH_SHORT).show();
                    }
                });
//       remove all product from cart
        removeAll.setOnClickListener(v -> {
            database.getReference().child("Users").child(auth.getUid()).child("Cart").removeValue();
            productList.clear();
            adapter.notifyDataSetChanged();
//            adapter1.notifyDataSetChanged();
            removeAll.setVisibility(View.GONE);
        });
//        call for buy product
        call.setOnClickListener(v -> database.getReference().child("Hatti").child("contact detail").child("phoneNo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.call_dialog_box);
                dialog.show();
                Button button = dialog.findViewById(R.id.btnCallDialogBoxButton);
                TextView msg = dialog.findViewById(R.id.tvCallDialogBoxMsg);
                msg.setText("Order Place by call");
                button.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    String phoneNumber=snapshot.getValue(String.class);
                    {

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, 101);

                            return;
                        }
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Objects.requireNonNull(phoneNumber).trim()));
                        startActivity(callIntent);
                    }
                });



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));

        return view;
    }

}