package com.echatti.hatti.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.echatti.hatti.R;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {
    private final List<String> productImagesList;

    public ProductImageAdapter(List<String> productImagesList) {
        this.productImagesList = productImagesList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView productImage = new ImageView(container.getContext());
        productImage.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(container).load(productImagesList.get(position)).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(productImage);
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
