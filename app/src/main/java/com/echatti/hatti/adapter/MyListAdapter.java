package com.echatti.hatti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.echatti.hatti.R;
import com.echatti.hatti.activity.ProductDetailsActivity;
import com.echatti.hatti.models.CartModel;
import com.echatti.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    ArrayList<categoryProductModel> list;
    Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public MyListAdapter(ArrayList<categoryProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_list_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {
        categoryProductModel model = list.get(position);
        Glide.with(context).load(list.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.productImage);
        holder.productName.setText(model.getName());
        holder.productPrice.setText(model.getPrice());
        holder.productMrp.setText(model.getMrp());
        holder.productDescription.setText(model.getDescription());
        holder.add.setOnClickListener(v -> {
            String storeCategory = list.get(position).getCategory();
            String storeId = list.get(position).getProductId();
            int qty = 1;
            CartModel cartModel = new CartModel(storeCategory,storeId,qty);
            database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(storeCategory+storeId)
                    .setValue(cartModel)
                    .addOnCompleteListener(task -> Toast.makeText(context,"Add On Cart", Toast.LENGTH_SHORT).show());
        });
        holder.remove.setOnClickListener(v -> database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("My List").child("list").child(list.get(position).getCategory()+list.get(position).getProductId()).removeValue());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("category",list.get(position).getCategory());
            intent.putExtra("id",list.get(position).getProductId());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice,productMrp,productDescription;
        ImageView productImage,remove;
        AppCompatButton add;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.ivListProductImage);
            productName = itemView.findViewById(R.id.tvListProductName);
            productMrp = itemView.findViewById(R.id.tvListProductMrp);
            productDescription = itemView.findViewById(R.id.tvListProductDescription);
            productPrice = itemView.findViewById(R.id.tvListProductPrice);
            add = itemView.findViewById(R.id.btnListProductAdd);
            remove = itemView.findViewById(R.id.ivListProductRemove);
            
        }
    }
}
