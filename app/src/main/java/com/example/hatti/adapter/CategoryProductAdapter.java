package com.example.hatti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hatti.R;
import com.example.hatti.activity.BuyDetailActivity;
import com.example.hatti.activity.ProductDetailsActivity;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.categoryProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>{
    private ArrayList<categoryProductModel> list;
    private Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
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
        Glide.with(context).load(list.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_home_24)).into(holder.imageView);
        holder.productName.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.price.setText(model.getPrice());
        holder.mrp.setText(model.getMrp());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProductDetailsActivity.class);
                intent.putExtra("category",list.get(position).getCategory());
                intent.putExtra("id",list.get(position).getProductId());
                context.startActivity(intent);
            }
        });
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeCategory = list.get(position).getCategory();
                String storeId = list.get(position).getProductId();
                int qty = 1;
                CartModel cartModel = new CartModel(storeCategory,storeId,qty);
                database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(storeCategory+storeId)
                        .setValue(cartModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task){
                                Toast.makeText(context,"Add On Cart", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
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
        }
    }
}
