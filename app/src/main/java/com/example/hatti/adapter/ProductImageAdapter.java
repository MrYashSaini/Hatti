package com.example.hatti.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {
    private List<Integer> productImagesList;

    public ProductImageAdapter(List<Integer> productImagesList) {
        this.productImagesList = productImagesList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView productImage = new ImageView(container.getContext());
        productImage.setImageResource(productImagesList.get(position));
        container.addView(productImage,0);
        return productImage;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public int getCount() {
        return productImagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
