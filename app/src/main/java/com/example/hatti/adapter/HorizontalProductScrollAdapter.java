package com.example.hatti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.activity.ProductDetailsActivity;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.HorizontalProductScrollModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {
    private List<HorizontalProductScrollModel> horizontalProductScrollModelsList;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelsList) {
        this.horizontalProductScrollModelsList = horizontalProductScrollModelsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productTitle.setText(horizontalProductScrollModelsList.get(position).getName());
        holder.productPrice.setText(horizontalProductScrollModelsList.get(position).getPrice());
        if(horizontalProductScrollModelsList.get(position).getImage().equals("default")){
            holder.productImage.setImageResource(R.drawable.ic_baseline_home_24);
        }
        else {
            Picasso.get().load(horizontalProductScrollModelsList.get(position).getImage()).placeholder(R.drawable.ic_baseline_home_24).into(holder.productImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ProductDetailsActivity.class);
                intent.putExtra("category",horizontalProductScrollModelsList.get(position).getCategory());
                intent.putExtra("id",horizontalProductScrollModelsList.get(position).getProductId());

                v.getContext().startActivity(intent);
            }
        });
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeCategory = horizontalProductScrollModelsList.get(position).getCategory();
                String storeId = horizontalProductScrollModelsList.get(position).getProductId();
                int qty = 1;
                CartModel cartModel = new CartModel(storeCategory,storeId,qty);
                database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(storeCategory+storeId)
                        .setValue(cartModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task){
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalProductScrollModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
