package com.example.hatti.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.activity.BuyConformActivity;
import com.example.hatti.activity.BuyDetailActivity;
import com.example.hatti.adapter.CartAdapter;
import com.example.hatti.adapter.CategoryProductAdapter;
import com.example.hatti.adapter.HorizontalProductScrollAdapter;
import com.example.hatti.adapter.ProductPriceListAdapter;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.HorizontalProductScrollModel;
import com.example.hatti.models.ProductListPriceModel;
import com.example.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView rvProductShow,rvProductPrice;
    ArrayList<categoryProductModel> productList = new ArrayList<>();
    ArrayList<ProductListPriceModel> productPriceList = new ArrayList<>();
    TextView buy,removeAll,tvTotalPrice,tvTotalMrp,tvTotalDiscount;
    int totalAmount= 0,totalMrp=0,totalDiscount=0;
    // recommended
    private TextView horizontalLayoutTitle;
    private Button btnViewAll;
    private RecyclerView rvProductLayout;
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
// for product Recycler View
        CartAdapter adapter = new CartAdapter(productList,getContext());
        rvProductShow.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvProductShow.setLayoutManager(linearLayoutManager);
// Recycler View for product price list
        ProductPriceListAdapter adapter1 = new ProductPriceListAdapter(productPriceList,getContext());
        rvProductPrice.setAdapter(adapter1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        rvProductPrice.setLayoutManager(linearLayoutManager1);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),BuyDetailActivity.class);
//                intent.putExtra("list",productList);
                intent.putExtra("Amount",98);
                intent.putExtra("discount",totalDiscount);
                intent.putExtra("mrp",totalMrp);
                startActivity(intent);
            }
        });

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
                                            categoryProductModel full = new categoryProductModel();
                                            full.setImage(productDetails.getImage());
                                            full.setName(productDetails.getName());
                                            full.setPrice(productDetails.getPrice());
                                            full.setMrp(productDetails.getMrp());
                                            full.setProductId(productDetails.getProductId());
                                            full.setCategory(productDetails.getCategory());
                                            full.setQty(products.getQty());
                                            productList.add(full);

                                            ProductListPriceModel itemList = new ProductListPriceModel();
                                            itemList.setImage(productDetails.getImage());
                                            itemList.setName(productDetails.getName());
                                            itemList.setPrice(productDetails.getPrice());
                                            itemList.setMrp(productDetails.getMrp());
                                            itemList.setQty(products.getQty()+"");


                                            Integer amount = Integer.valueOf(itemList.getPrice());
                                            int price = amount.intValue();
                                            int total = (price * products.getQty());
                                            totalAmount +=total;
                                            itemList.setTotalPrice(total+"");
                                            productPriceList.add(itemList);
//                                            Integer amount = Integer.valueOf(full.getPrice());
//                                            int price = amount.intValue();
//                                            totalAmount += (price * products.getQty());

                                            Integer mrpAmount = Integer.valueOf(full.getMrp());
                                            int mrp = mrpAmount.intValue();
                                            totalMrp = totalMrp+(mrp*products.getQty());

                                            totalDiscount = totalDiscount+((mrp*products.getQty())-(price*products.getQty()));
                                            tvTotalDiscount.setText(totalDiscount+"");
                                            tvTotalPrice.setText(totalAmount+"");
                                            tvTotalMrp.setText(totalMrp+"");
                                            adapter.notifyDataSetChanged();
                                            adapter1.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getContext(), "not get data from product category", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        adapter.notifyDataSetChanged();



                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "not get data from cart", Toast.LENGTH_SHORT).show();
                    }
                });

        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Users").child(auth.getUid()).child("Cart").removeValue();
                productList.clear();
                adapter.notifyDataSetChanged();
            }
        });

//        Toast.makeText(getContext(), "list size"+productList.size(), Toast.LENGTH_SHORT).show();
//        horizontalLayoutTitle = view.findViewById(R.id.horizontalScrollViewTitle);
//        horizontalLayoutTitle.setText(R.string.recommended);
//        btnViewAll = view.findViewById(R.id.btnHorizontalScrollViewButton);
//        btnViewAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference();
//                myRef.child("Users").child(auth.getUid()).child("CategoryShow").child("category").setValue("recommended");
//                AppCompatActivity activity = (AppCompatActivity)v.getContext();
//                CategoryItemsFragment fragment = new CategoryItemsFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null).commit();
//            }
//        });
//        rvProductLayout = view.findViewById(R.id.rvHorizontalView);
//        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
//
//        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
//        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
//        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rvProductLayout.setLayoutManager(linearLayoutManager2);
//        rvProductLayout.setAdapter(horizontalProductScrollAdapter);
//        database.getReference().child("categorys").child("product category").child("f11").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                horizontalProductScrollModelList.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    HorizontalProductScrollModel products = dataSnapshot.getValue(HorizontalProductScrollModel.class);
//                    horizontalProductScrollModelList.add(products);
//                }
//                horizontalProductScrollAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
    }
}