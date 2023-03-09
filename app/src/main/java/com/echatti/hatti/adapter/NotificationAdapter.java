package com.echatti.hatti.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.echatti.hatti.R;
import com.echatti.hatti.activity.NotificationDetailActivity;
import com.echatti.hatti.models.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<NotificationModel> list;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationModel model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.date.setText(model.getDate()+"  "+model.getTime());
        Glide.with(context).load(list.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationDetailActivity.class);
            intent.putExtra("date",model.getDate());
            intent.putExtra("time",model.getTime());
            intent.putExtra("title",model.getTitle());
            intent.putExtra("message",model.getDescription());
            intent.putExtra("image",model.getImage());
            intent.putExtra("type",model.getType());
            intent.putExtra("category",model.getCategory());
            intent.putExtra("id",model.getId()+"");
            intent.putExtra("notificationId",model.getNotificationId()+"");
            context.startActivity(intent);
        });
        try{
            if (model.isSeen()){
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFE3F3FF"));
            }

        }
        catch (Exception ignored){

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,date;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvNotificationTitle);
            description = itemView.findViewById(R.id.tvNotificationDescription);
            date = itemView.findViewById(R.id.tvNotificationDate);
            image= itemView.findViewById(R.id.ivNotificationImage);
        }
    }
}
