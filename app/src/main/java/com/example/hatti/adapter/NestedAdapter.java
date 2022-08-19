package com.example.hatti.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;

import java.util.List;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.ViewHolder> {
    private List<String> miniList;

    public NestedAdapter(List<String> miniList) {
        this.miniList = miniList;
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
                Toast.makeText(v.getContext(), position+":"+holder.miniCategory.getText(), Toast.LENGTH_SHORT).show();
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
