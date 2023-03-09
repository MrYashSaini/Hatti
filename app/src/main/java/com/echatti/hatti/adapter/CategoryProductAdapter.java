package com.echatti.hatti.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>{
    private final ArrayList<categoryProductModel> list;
    private final Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String authId = auth.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public CategoryProductAdapter(ArrayList<categoryProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_show,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryProductAdapter.ViewHolder holder, int position) {

        categoryProductModel model = list.get(position);
        String storeCategory = list.get(position).getCategory();
        String storeId = list.get(position).getProductId();
        Glide.with(context).load(model.getImage2()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.imageView);
        holder.productName.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.price.setText(model.getPrice());
        holder.mrp.setText(model.getMrp());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,ProductDetailsActivity.class);
            intent.putExtra("category",list.get(position).getCategory());
            intent.putExtra("id",list.get(position).getProductId());
            context.startActivity(intent);
        });
        holder.addButton.setOnClickListener(v -> {

            int qty = 1;
            CartModel cartModel = new CartModel(storeCategory,storeId,qty);
            database.getReference().child("Users").child(authId).child("Cart").child("list").child(storeCategory+storeId)
                    .setValue(cartModel)
                    .addOnCompleteListener(task -> Toast.makeText(context,"Add On Cart", Toast.LENGTH_SHORT).show());

        });
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(authId).child("My List").child("list").child(storeCategory+storeId);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // value exists in the database
                    holder.addToList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error);
            }
        });

        holder.addToList.setOnClickListener(view -> {
            CartModel cartModel = new CartModel(storeCategory,storeId,1);
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // value exists in the database
                        holder.addToList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CFCFCF")));
                        database.getReference().child("Users").child(authId).child("My List").child("list").child(storeCategory+storeId).removeValue()
                                .addOnCompleteListener(task -> Toast.makeText(context, "Remove Product", Toast.LENGTH_SHORT).show());

                    } else {
                        // value does not exist in the database
                        holder.addToList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                        database.getReference().child("Users").child(authId).child("My List").child("list").child(storeCategory+storeId)
                                .setValue(cartModel)
                                .addOnCompleteListener(task -> {
                                    holder.addToList.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                                    Toast.makeText(context, "Add On List", Toast.LENGTH_SHORT).show();
                                });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,addToList;
        TextView productName,description,price,mrp;
        AppCompatButton addButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivProductImageInCategory);
            productName = itemView.findViewById(R.id.tvProductNameInCategory);
            description = itemView.findViewById(R.id.tvProductDescriptionInCategory);
            price = itemView.findViewById(R.id.tvProductPriceInCategory);
            mrp  = itemView.findViewById(R.id.tvProductMrpInCategory);
            addButton = itemView.findViewById(R.id.btnProductAddButtonInCategory);
            addToList = itemView.findViewById(R.id.ivCategoryItemShowAddList);
        }
    }
}
