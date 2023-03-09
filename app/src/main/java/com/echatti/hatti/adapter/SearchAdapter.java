package com.echatti.hatti.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.echatti.hatti.activity.ProductDetailsActivity;
import com.echatti.hatti.models.SearchModel;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    ArrayList<SearchModel> list;

    public SearchAdapter(Context context, ArrayList<SearchModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setFilteredList(ArrayList<SearchModel> filteredList){
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        SearchModel model = list.get(position);
        holder.name.setText(model.getName());
        Glide.with(context).load(model.getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("category",model.getCategory());
                intent.putExtra("id",model.getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivSearchSampleProductImg);
            name = itemView.findViewById(R.id.tvSearchSampleProductName);
        }
    }
}
