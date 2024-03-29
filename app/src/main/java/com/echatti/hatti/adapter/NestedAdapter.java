package com.echatti.hatti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.echatti.hatti.R;
import com.echatti.hatti.fragments.CategoryItemsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.ViewHolder> {
    private List<String> miniList;
    Context context2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

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
                database.getReference().child("Users").child(auth.getUid()).child("CategoryShow").child("category").setValue(miniList.get(position));
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
