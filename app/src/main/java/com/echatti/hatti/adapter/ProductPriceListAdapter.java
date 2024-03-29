package com.echatti.hatti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.echatti.hatti.R;
import com.echatti.hatti.models.ProductListPriceModel;

import java.util.ArrayList;

public class ProductPriceListAdapter extends RecyclerView.Adapter<ProductPriceListAdapter.ViewHolder> {
    private final ArrayList<ProductListPriceModel> list;
    Context context;

    public ProductPriceListAdapter(ArrayList<ProductListPriceModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductPriceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_total_payment_items_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPriceListAdapter.ViewHolder holder, int position) {
        ProductListPriceModel model = list.get(position);
        Glide.with(context).load(list.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.image);
        holder.name.setText(model.getName());
        holder.price.setText("Rs. "+model.getPrice());
        holder.mrp.setText("Rs. "+model.getMrp());
        holder.qty.setText(model.getQty());
        holder.totalPrice.setText("Rs. "+model.getTotalPrice());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,price,mrp,qty,totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivPriceListImage);
            price = itemView.findViewById(R.id.tvPriceListPrice);
            mrp = itemView.findViewById(R.id.tvPriceListMrp);
            qty = itemView.findViewById(R.id.tvPriceListQty);
            totalPrice = itemView.findViewById(R.id.tvPriceListTotalPrice);
            name = itemView.findViewById(R.id.tvPriceListName);

        }
    }
}
