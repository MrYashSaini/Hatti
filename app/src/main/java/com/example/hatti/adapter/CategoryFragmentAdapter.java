package com.example.hatti.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.ViewHolder> {
    private List<CategoryModel> categoryModelList;
    private List<String> list = new ArrayList<>();

    public CategoryFragmentAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_name_sample,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel model = categoryModelList.get(position);
        holder.mainCategory.setText(model.getItemText());

        boolean isExpandable = model.isExpandable();
        holder.relativeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable){
            holder.arrowImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up);
        }
        else {
            holder.arrowImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down);
        }
        NestedAdapter adapter = new NestedAdapter(list);
        holder.miniCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.miniCategoryRecyclerView.setHasFixedSize(true);
        holder.miniCategoryRecyclerView.setAdapter(adapter);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                list = model.getNestedList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        TextView mainCategory;
        RecyclerView miniCategoryRecyclerView;
        ImageView arrowImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayoutForCategoryFragment);
            relativeLayout = itemView.findViewById(R.id.rlExpandLayout);
            mainCategory = itemView.findViewById(R.id.tvMainCategoryName);
            miniCategoryRecyclerView = itemView.findViewById(R.id.rvMiniCategoryRecyclerView);
            arrowImage = itemView.findViewById(R.id.ivExpandStatus);
        }
    }
}
