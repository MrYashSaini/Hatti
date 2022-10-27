package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.adapter.HorizontalProductScrollAdapter;
import com.example.hatti.adapter.ProductImageAdapter;
import com.example.hatti.fragments.CategoryItemsFragment;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.HorizontalProductScrollModel;
import com.example.hatti.models.categoryProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager vpProductImage;
    private TabLayout tlProductIndicator;
    ImageView ivAddOnMyList;
    TextView tvAddOnCart,name,price,stock,description,mrp,quantity,si,weight,brand,manufacture,ingredient;
    String storeCategory,storeId;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<String> productImagesList=new ArrayList<>();
    private TextView horizontalLayoutTitle;
    private Button btnViewAll;
    private RecyclerView rvProductLayout;
    ImageButton increase,decrease;
    EditText noOfProduct;
    boolean qtyOk = false;
    int minQty = 1;
    int maxQty = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        storeCategory = intent.getStringExtra("category");
        storeId = intent.getStringExtra("id");


        // initialize
        vpProductImage = findViewById(R.id.vpProductImages);
        tlProductIndicator = findViewById(R.id.tlProductIndicator);
        ivAddOnMyList = findViewById(R.id.ivAddToMyList);
        tvAddOnCart= findViewById(R.id.tvAddToCart);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        increase = findViewById(R.id.ibIncreaseQty);
        decrease = findViewById(R.id.ibDecreaseQty);
        noOfProduct = findViewById(R.id.etQtyOfProduct);

        name = findViewById(R.id.tvProductNameInDetail);
        price = findViewById(R.id.tvProductPriceInDetail);
        mrp = findViewById(R.id.tvProductMrpInDetail);
        description = findViewById(R.id.tvProductDescriptionInDetail);
        quantity = findViewById(R.id.tvQuantityInDetail);
        stock = findViewById(R.id.tvStock);
        si = findViewById(R.id.tvProductSiInDetail);
        weight = findViewById(R.id.tvWeight);
        brand = findViewById(R.id.tvProductBrandInDetail);
        manufacture = findViewById(R.id.tvProductManufacturerInDetail);
        ingredient = findViewById(R.id.tvProductIngredientInDetail);

        ProductImageAdapter adapter = new ProductImageAdapter(productImagesList);
        vpProductImage.setAdapter(adapter);
        // connect tab layout to view pager
        tlProductIndicator.setupWithViewPager(vpProductImage,true);

        database.getReference().child("categorys").child("product category").child(storeCategory).child(storeId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryProductModel model = snapshot.getValue(categoryProductModel.class);
                        name.setText(model.getName().toUpperCase(Locale.ROOT));
                        price.setText(model.getPrice());
                        mrp.setText(model.getMrp());
                        description.setText(model.getDescription());
                        quantity.setText(model.getQuantity());
                        stock.setText(model.getStock());
                        weight.setText(model.getQuantity());
                        brand.setText(model.getBrand());
                        manufacture.setText(model.getManufacturer());
                        si.setText(model.getSi());
                        ingredient.setText(model.getIngredient());
                        productImagesList.add(model.getImage());
                        productImagesList.add(model.getImage2());
                        productImagesList.add(model.getImage3());
                        productImagesList.add(model.getImage4());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        // Add On Cart
        tvAddOnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productQty = qtyFun();
                if(qtyOk){
                    CartModel cartModel = new CartModel(storeCategory,storeId,productQty);
                    database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(storeCategory+storeId)
                            .setValue(cartModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ProductDetailsActivity.this, "Add On Cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        // Add On MyList
        ivAddOnMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel cartModel = new CartModel(storeCategory,storeId,1);
                database.getReference().child("Users").child(auth.getUid()).child("My List").child("list").child(storeCategory+storeId)
                        .setValue(cartModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProductDetailsActivity.this, "Add On List", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Qty = qtyFun();
                if(Qty<maxQty){
                    noOfProduct.setText((Qty+1)+"");
                }
                else{
                    noOfProduct.setText("500");
                }
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Qty = qtyFun();
                if(Qty>minQty){
                    noOfProduct.setText((Qty-1)+"");
                }
                else{
                    noOfProduct.setText("1");
                }
            }
        });

        // horizontal product view
        horizontalLayoutTitle = findViewById(R.id.horizontalScrollViewTitle);
        horizontalLayoutTitle.setText(R.string.recommended);
        btnViewAll = findViewById(R.id.btnHorizontalScrollViewButton);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                CategoryItemsFragment fragment = new CategoryItemsFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null).commit();
            }
        });
        rvProductLayout = findViewById(R.id.rvHorizontalView);
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();

        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvProductLayout.setLayoutManager(linearLayoutManager);
        rvProductLayout.setAdapter(horizontalProductScrollAdapter);
        database.getReference().child("categorys").child("product category").child(storeCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                horizontalProductScrollModelList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HorizontalProductScrollModel products = dataSnapshot.getValue(HorizontalProductScrollModel.class);
                    horizontalProductScrollModelList.add(products);
                }
                horizontalProductScrollAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//--------------------------------------------
//2 horizontal scroll view
        TextView horizontalLayoutTitle2 = findViewById(R.id.horizontalScrollViewTitle2);
        horizontalLayoutTitle2.setText("Top Products");
        Button btnViewAll2 = findViewById(R.id.btnHorizontalScrollViewButton2);
        btnViewAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                CategoryItemsFragment fragment = new CategoryItemsFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null).commit();
            }
        });
        RecyclerView rvSecondHV = findViewById(R.id.rvHorizontalView2);
        List<HorizontalProductScrollModel> horizontalProductScrollModelList2 = new ArrayList<>();

        HorizontalProductScrollAdapter horizontalProductScrollAdapter2 = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSecondHV.setLayoutManager(linearLayoutManager2);
        rvSecondHV.setAdapter(horizontalProductScrollAdapter2);
        database.getReference().child("categorys").child("product category").child(storeCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                horizontalProductScrollModelList2.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HorizontalProductScrollModel products = dataSnapshot.getValue(HorizontalProductScrollModel.class);
                    horizontalProductScrollModelList2.add(products);
                }
                horizontalProductScrollAdapter2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private int qtyFun() {
        int productQty = 0;
        try{
            productQty = Integer.parseInt(String.valueOf(noOfProduct.getText()));
        }
        catch (Exception e){
            Float obj = Float.valueOf(String.valueOf(noOfProduct.getText()));
            productQty = obj.intValue();
        }
        if(productQty<minQty){
            Toast.makeText(ProductDetailsActivity.this, "select qty more then "+minQty, Toast.LENGTH_SHORT).show();
        }
        else if (productQty>maxQty){
            Toast.makeText(ProductDetailsActivity.this, "select maximum qty "+maxQty, Toast.LENGTH_SHORT).show();
        }
        else{
            qtyOk = true;
        }
        return productQty;
    }
}