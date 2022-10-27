package com.example.hatti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.activity.OrderDetailActivity;
import com.example.hatti.models.OrderModel;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderModel> list;

    public OrderAdapter(Context context, ArrayList<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        OrderModel model = list.get(position);
        holder.noOfProduct.setText("("+model.getNoOfProduct()+")");
        holder.status.setText(model.getStatus());
        holder.Amount.setText(model.getPrice());
        holder.DateTime.setText(model.getDate()+"   "+model.getTime());
        holder.viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("time",model.getTime());
                intent.putExtra("date",model.getDate());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noOfProduct,Amount,DateTime,status;
        AppCompatButton viewDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noOfProduct = itemView.findViewById(R.id.tvOrderNoOfProduct);
            Amount = itemView.findViewById(R.id.tvOrderAmount);
            DateTime = itemView.findViewById(R.id.tvOrderDateAndTime);
            status = itemView.findViewById(R.id.tvOrderDeliverStatus);
            viewDetail = itemView.findViewById(R.id.btnOrderViewDetail);
        }
    }
}
