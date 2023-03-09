package com.echatti.hatti.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.echatti.hatti.R;
import com.echatti.hatti.account.ProfileSetupActivity;
import com.echatti.hatti.adapter.HorizontalProductScrollAdapter;
import com.echatti.hatti.adapter.ProductImageAdapter;
import com.echatti.hatti.internet.NetworkBroadcast;
import com.echatti.hatti.models.CartModel;
import com.echatti.hatti.models.HorizontalProductScrollModel;
import com.echatti.hatti.models.categoryProductModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView ivAddOnMyList;
    private TextView tvAddOnCart,name,price,stock,description,mrp,quantity,si,weight,brand,manufacture,ingredient;
    String storeCategory,storeId;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<String> productImagesList=new ArrayList<>();
    ImageButton increase,decrease;
    EditText noOfProduct;
    boolean qtyOk = false;
    int minQty = 1;
    int maxQty = 500;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        storeCategory = intent.getStringExtra("category");
        storeId = intent.getStringExtra("id");
        // initialize
        ViewPager vpProductImage = findViewById(R.id.vpProductImages);
        TabLayout tlProductIndicator = findViewById(R.id.tlProductIndicator);
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

        String authId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
//      check product in list
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(authId).child("My List").child("list").child(storeCategory+storeId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // value exists in the database
                    ivAddOnMyList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error);
            }
        });

//        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ProductImageAdapter adapter = new ProductImageAdapter(productImagesList);
        vpProductImage.setAdapter(adapter);
        // connect tab layout to view pager
        tlProductIndicator.setupWithViewPager(vpProductImage,true);

        database.getReference().child("categorys").child("product category").child(storeCategory).child(storeId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryProductModel model = snapshot.getValue(categoryProductModel.class);
                        name.setText(Objects.requireNonNull(model).getName().toUpperCase(Locale.ROOT));
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
        tvAddOnCart.setOnClickListener(v -> {
            int productQty = qtyFun();
            if(qtyOk){
                CartModel cartModel = new CartModel(storeCategory,storeId,productQty);
                database.getReference().child("Users").child(authId).child("Cart").child("list").child(storeCategory+storeId)
                        .setValue(cartModel)
                        .addOnCompleteListener(task -> Toast.makeText(ProductDetailsActivity.this, "Add On Cart", Toast.LENGTH_SHORT).show());
            }
        });
        // Add On MyList
        ivAddOnMyList.setOnClickListener(v -> {
            CartModel cartModel = new CartModel(storeCategory,storeId,1);
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // value exists in the database
                        ivAddOnMyList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CFCFCF")));
                        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("My List").child("list").child(storeCategory+storeId).removeValue()
                                .addOnCompleteListener(task -> Toast.makeText(ProductDetailsActivity.this, "Remove Product", Toast.LENGTH_SHORT).show());

                    } else {
                        // value does not exist in the database
                        ivAddOnMyList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                        database.getReference().child("Users").child(authId).child("My List").child("list").child(storeCategory+storeId)
                                .setValue(cartModel)
                                .addOnCompleteListener(task -> {
                                    ivAddOnMyList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                                    Toast.makeText(ProductDetailsActivity.this, "Add On List", Toast.LENGTH_SHORT).show();
                                });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error);
                }
            });

        });

        increase.setOnClickListener(v -> {
            int Qty = qtyFun();
            if(Qty<maxQty){
                noOfProduct.setText((Qty+1)+"");
            }
            else{
                noOfProduct.setText("500");
            }
        });
        decrease.setOnClickListener(v -> {
            int Qty = qtyFun();
            if(Qty>minQty){
                noOfProduct.setText((Qty-1)+"");
            }
            else{
                noOfProduct.setText("1");
            }
        });

        // horizontal product view
        TextView horizontalLayoutTitle = findViewById(R.id.horizontalScrollViewTitle);
        horizontalLayoutTitle.setText("More Product");
        Button btnViewAll = findViewById(R.id.btnHorizontalScrollViewButton);
        btnViewAll.setVisibility(View.GONE);

        RecyclerView rvProductLayout = findViewById(R.id.rvHorizontalView);
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();

        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList,ProductDetailsActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvProductLayout.setLayoutManager(linearLayoutManager);
        rvProductLayout.setAdapter(horizontalProductScrollAdapter);
        database.getReference().child("Hatti").child("recommended products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                horizontalProductScrollModelList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartModel product = dataSnapshot.getValue(CartModel.class);
                    database.getReference().child("categorys").child("product category").child(product.getCategory()).child(product.getProductId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HorizontalProductScrollModel productDetail = snapshot.getValue(HorizontalProductScrollModel.class);
                                    horizontalProductScrollModelList.add(productDetail);
                                    horizontalProductScrollAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(ProductDetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private int qtyFun() {
        int productQty = 0;
        try{
            productQty = Integer.parseInt(String.valueOf(noOfProduct.getText()));
        }
        catch (Exception e){
            float obj = Float.parseFloat(String.valueOf(noOfProduct.getText()));
            productQty = (int) obj;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.tolProfile){
            Intent intent = new Intent(ProductDetailsActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(ProductDetailsActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(ProductDetailsActivity.this, AboutActivity.class);
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