package com.example.hatti.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hatti.MainActivity;
import com.example.hatti.R;
import com.example.hatti.account.ProfileSetupActivity;
import com.example.hatti.internet.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NotificationDetailActivity extends AppCompatActivity {
    TextView date,title,message;
    Button exit,check;
    ImageView image;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        date = findViewById(R.id.tvNotificationDetailsDate);
        title = findViewById(R.id.tvNotificationDetailsTitle);
        message = findViewById(R.id.tvNotificationDetailsMessage);
        exit = findViewById(R.id.btnNotificationDetailsExit);
        image = findViewById(R.id.ivNotificationDetailImage);
        check = findViewById(R.id.btnNotificationDetailCheck);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String strDate = intent.getStringExtra("date");
        String strTime = intent.getStringExtra("time");
        String strTitle = intent.getStringExtra("title");
        String strMessage = intent.getStringExtra("message");
        String strImage = intent.getStringExtra("image");
        String strType = intent.getStringExtra("type");
        String strId = intent.getStringExtra("id");
        String strCategory = intent.getStringExtra("category");
        String notificationId = intent.getStringExtra("notificationId");
        if (strType == null){
            check.setVisibility(View.GONE);
        }

//        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        date.setText(strDate+"   "+strTime);
        title.setText(strTitle);
        message.setText(strMessage);
        Glide.with(NotificationDetailActivity.this).load(strImage).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(image);
        database.getReference().child("Users").child(auth.getUid()).child("Notification").child("mini notification")
                        .child(notificationId).child("seen").setValue(true);

        exit.setOnClickListener(v -> startActivity(new Intent(NotificationDetailActivity.this, MainActivity.class)));
        check.setOnClickListener(v -> {
            switch (Objects.requireNonNull(strType)){
                case "Order":
                    Intent intent1 = new Intent(NotificationDetailActivity.this, OrderDetailActivity.class);
                    intent1.putExtra("price","0");
                    intent1.putExtra("orderId",strId);
                    startActivity(intent1);
                    break;

                case "Payment":
                    Intent intent2 = new Intent(NotificationDetailActivity.this,PaymentWalletActivity.class);
                    startActivity(intent2);
                    break;
                case "Product":
                    Intent intent3 = new Intent(NotificationDetailActivity.this,ProductDetailsActivity.class);
                    intent3.putExtra("category",strCategory);
                    intent3.putExtra("id",strId);
                    startActivity(intent3);
                    break;
                case "Profile":
                    Intent intent4 = new Intent(NotificationDetailActivity.this, ProfileSetupActivity.class);
                    startActivity(intent4);
                    break;
                default:
                    check.setVisibility(View.GONE);

            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}