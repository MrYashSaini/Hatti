package com.example.hatti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hatti.R;
import com.example.hatti.models.PaymentModel;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    Context context;
    ArrayList<PaymentModel> list;

    public PaymentAdapter(Context context, ArrayList<PaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_payment_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        PaymentModel model = list.get(position);
        holder.dateTime.setText(model.getDate()+"   "+model.getTime());
        holder.noOfProduct.setText("("+model.getNoOfProduct()+")");
        holder.amount.setText("Rs. "+model.getAmount());
        holder.pay.setText("Rs. "+model.getPay());
        holder.due.setText("Rs. "+model.getDue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTime,noOfProduct,amount,pay,due;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime  = itemView.findViewById(R.id.tvPaymentDateAndTime);
            noOfProduct = itemView.findViewById(R.id.tvPaymentNoOfProduct);
            amount = itemView.findViewById(R.id.tvPaymentTotalAmount);
            pay = itemView.findViewById(R.id.tvPaymentPayAmount);
            due = itemView.findViewById(R.id.tvPaymentDueAmount);
        }
    }
}
