package com.example.hatti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hatti.R;
import com.example.hatti.fragments.CategoryItemsFragment;
import com.example.hatti.models.CategoryMenuModel;
import com.example.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("Users").child(auth.getUid()).child("CategoryShow").child("category").setValue(model.getCategoryName());
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                CategoryItemsFragment fragment = new CategoryItemsFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null).commit();

            }
        });
        Glide.with(context).load(categoryMenuModelList.get(position).getCategoryIconLink()).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_home_24)).into(holder.categoryIcon);
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
