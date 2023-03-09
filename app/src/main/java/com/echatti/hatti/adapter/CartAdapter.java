package com.echatti.hatti.adapter;

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
import com.echatti.hatti.R;
import com.echatti.hatti.activity.ProductDetailsActivity;
import com.echatti.hatti.models.categoryProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final ArrayList<categoryProductModel> list;
    private final Context context;
    private final RecyclerView recyclerView;
    TextView refresh;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public CartAdapter(ArrayList<categoryProductModel> list, Context context,RecyclerView recyclerView,TextView refresh) {
        this.list = list;
        this.context = context;
        this.recyclerView = recyclerView;
        this.refresh = refresh;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        String authId = auth.getUid();
        int maxQty = 500;
        int minQty = 1;
        categoryProductModel model = list.get(position);
        Glide.with(context).load(list.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.productImage);
        holder.productName.setText(model.getName());
        holder.productPrice.setText(model.getPrice());
        holder.productMrp.setText(model.getMrp());
        String q = Integer.toString(model.getQty());
        holder.productQty.setText(q);
        holder.increase.setOnClickListener(v -> {
            refresh.setVisibility(View.VISIBLE);
            String qty = String.valueOf(holder.productQty.getText());
            int productQty;
            try{
                productQty = Integer.parseInt(qty);
            }
            catch (Exception e){
                float obj = Float.parseFloat(qty);
                productQty = (int) obj;
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
            database.getReference().child("Users").child(authId).child("Cart").child("list").child(model.getCategory()+model.getProductId()).setValue(model1)
                    .addOnCompleteListener(task -> {
                        recyclerView.smoothScrollToPosition(position+1);
                        refresh.setVisibility(View.GONE);
                    });
        });
        holder.decrease.setOnClickListener(v -> {
            refresh.setVisibility(View.VISIBLE);
            String qty = String.valueOf(holder.productQty.getText());
            int productQty;
            try{
                productQty = Integer.parseInt(qty);
            }
            catch (Exception e){
                float obj = Float.parseFloat(qty);
                productQty = (int) obj;
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
            database.getReference().child("Users").child(authId).child("Cart").child("list").child(model.getCategory()+model.getProductId()).setValue(model1)
                    .addOnCompleteListener(task -> {
                        recyclerView.smoothScrollToPosition(position+1);
                        refresh.setVisibility(View.GONE);
                    });
//            notifyDataSetChanged();
            
        });
        holder.removeButton.setOnClickListener(v -> {
            database.getReference().child("Users").child(authId).child("Cart").child("list").child(model.getCategory()+model.getProductId()).removeValue();
            notifyDataSetChanged();
//            notifyItemRemoved(position);
        });
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
