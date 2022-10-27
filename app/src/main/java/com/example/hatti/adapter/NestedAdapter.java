package com.example.hatti.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.activity.MyListActivity;
import com.example.hatti.fragments.CategoryItemsFragment;

import java.util.List;


public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.ViewHolder> {
    private List<String> miniList;
    Context context2;

    public NestedAdapter(List<String> miniList,Context context2) {
        this.miniList = miniList;
        this.context2 = context2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_all_items_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.miniCategory.setText(miniList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                CategoryItemsFragment fragment = new CategoryItemsFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return miniList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView miniCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            miniCategory = itemView.findViewById(R.id.tvMiniCategoryName);
        }
    }
}
