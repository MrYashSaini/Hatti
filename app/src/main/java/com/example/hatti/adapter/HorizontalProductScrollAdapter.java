package com.example.hatti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.activity.ProductDetailsActivity;
import com.example.hatti.models.HorizontalProductScrollModel;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {
    private List<HorizontalProductScrollModel> horizontalProductScrollModelsList;
    Context context;
    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelsList) {
        this.horizontalProductScrollModelsList = horizontalProductScrollModelsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productTitle.setText(horizontalProductScrollModelsList.get(position).getProductTitle());
        holder.productPrice.setText(horizontalProductScrollModelsList.get(position).getProductPrice());
        holder.productImage.setImageResource(horizontalProductScrollModelsList.get(position).getProductImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalProductScrollModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle,productPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.ivHsProductImage);
            productTitle = itemView.findViewById(R.id.tvHsItemName);
            productPrice = itemView.findViewById(R.id.tvHsPrice);
        }
    }
}
