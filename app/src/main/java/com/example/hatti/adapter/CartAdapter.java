package com.example.hatti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hatti.R;
import com.example.hatti.activity.ProductDetailsActivity;
import com.example.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<categoryProductModel> list;
    private Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public CartAdapter(ArrayList<categoryProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        int maxQty = 500;
        int minQty = 1;
        categoryProductModel model = list.get(position);
        Glide.with(context).load(list.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_home_24)).into(holder.productImage);
        holder.productName.setText(model.getName());
        holder.productPrice.setText(model.getPrice());
        holder.productMrp.setText(model.getMrp());
        String q = Integer.toString(model.getQty());
        holder.productQty.setText(q);
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = String.valueOf(holder.productQty.getText());
                int productQty = 0;
                try{
                    productQty = Integer.parseInt(qty);
                }
                catch (Exception e){
                    Float obj = Float.valueOf(qty);
                    productQty = obj.intValue();
                }
                if (productQty<maxQty){
                    productQty += 1;
                    holder.productQty.setText(productQty+"");
                }
                else {
                    holder.productQty.setText(maxQty+"");
                }
                categoryProductModel model1 = new categoryProductModel();
                model1.setQty(productQty);
                model1.setCategory(model.getCategory());
                model1.setProductId(model.getProductId());
                database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(model.getCategory()+model.getProductId()).setValue(model1);
            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = String.valueOf(holder.productQty.getText());
                int productQty = 0;
                try{
                    productQty = Integer.parseInt(qty);
                }
                catch (Exception e){
                    Float obj = Float.valueOf(qty);
                    productQty = obj.intValue();
                }
                if (productQty>minQty ){
                    productQty -= 1;
                    holder.productQty.setText(productQty+"");
                }
                else {
                    holder.productQty.setText(minQty+"");
                }
                categoryProductModel model1 = new categoryProductModel();
                model1.setQty(productQty);
                model1.setCategory(model.getCategory());
                model1.setProductId(model.getProductId());
                database.getReference().child("Users").child(auth.getUid()).child("Cart").child("list").child(model.getCategory()+model.getProductId()).setValue(model1);

            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Users").child(auth.getUid()).child("Cart").child(model.getCategory()+model.getProductId()).removeValue();
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("category",list.get(position).getCategory());
                intent.putExtra("id",list.get(position).getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage,increase,decrease;
        TextView productName,productPrice,productMrp;
        EditText productQty;
        AppCompatButton removeButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.ivCartProductImage);
            productName = itemView.findViewById(R.id.tvCartProductName);
            productPrice = itemView.findViewById(R.id.tvCartProductPrice);
            productMrp = itemView.findViewById(R.id.tvCartProductMrp);
            productQty = itemView.findViewById(R.id.etCartProductQty);
            removeButton = itemView.findViewById(R.id.btnCartProductRemove);
            increase = itemView.findViewById(R.id.ivCartIncreaseQty);
            decrease = itemView.findViewById(R.id.ivCartDecreaseQty);
        }
    }
}
