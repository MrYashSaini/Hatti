package com.example.hatti.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.hatti.R;
import com.example.hatti.adapter.ProductImageAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager vpProductImage;
    private TabLayout tlProductIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // initialize
        vpProductImage = findViewById(R.id.vpProductImages);
        tlProductIndicator = findViewById(R.id.tlProductIndicator);
        //
        List<Integer> productImagesList=new ArrayList<>();
        productImagesList.add(R.drawable.banner);
        productImagesList.add(R.drawable.logo);
        productImagesList.add(R.drawable.ic_baseline_home_24);
        productImagesList.add(R.drawable.ic_baseline_mail_24);

        ProductImageAdapter adapter = new ProductImageAdapter(productImagesList);
        vpProductImage.setAdapter(adapter);

        //
        // connect tab layout to view pager
        tlProductIndicator.setupWithViewPager(vpProductImage,true);
    }
}