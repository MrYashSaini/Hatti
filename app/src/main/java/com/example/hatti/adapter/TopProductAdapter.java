package com.example.hatti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.models.HorizontalProductScrollModel;
import com.example.hatti.models.categoryProductModel;

import java.util.ArrayList;

public class TopProductAdapter extends RecyclerView.Adapter<TopProductAdapter.ViewHolder> {
    ArrayList<categoryProductModel> list;
    Context context;

    public TopProductAdapter(ArrayList<categoryProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TopProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_scroll_layout2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopProductAdapter.ViewHolder holder, int position) {
        categoryProductModel model = list.get(position);
        holder.productTitle.setText(model.getName());
        holder.productPrice.setText(model.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle,productPrice;
        AppCompatButton addButton,seeAll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.ivHsProductImage);
            productTitle = itemView.findViewById(R.id.tvHsItemName);
            productPrice = itemView.findViewById(R.id.tvHsPrice);
            addButton = itemView.findViewById(R.id.btnAddProductHv);
            seeAll = itemView.findViewById(R.id.btnHorizontalScrollViewButton);
        }
    }
}
