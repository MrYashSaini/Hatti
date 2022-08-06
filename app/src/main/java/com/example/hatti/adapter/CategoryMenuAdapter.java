package com.example.hatti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.models.CategoryMenuModel;

import java.util.List;

public class CategoryMenuAdapter extends RecyclerView.Adapter<CategoryMenuAdapter.ViewHolder> {
    private List<CategoryMenuModel> categoryMenuModelList;
    Context context;

    public CategoryMenuAdapter(List<CategoryMenuModel> categoryMenuModelList, Context context) {
        this.categoryMenuModelList = categoryMenuModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryMenuAdapter.ViewHolder holder, int position) {
        CategoryMenuModel model = categoryMenuModelList.get(position);
        holder.categoryName.setText(model.getCategoryName());
//        holder.categoryIcon.set

    }

    @Override
    public int getItemCount() {
        return categoryMenuModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryIcon;
        private TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            categoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
