package com.echatti.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.echatti.hatti.MainActivity;
import com.echatti.hatti.R;
import com.echatti.hatti.account.ProfileSetupActivity;
import com.echatti.hatti.internet.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NotificationDetailActivity extends AppCompatActivity {
    TextView date,title,message;
    Button exit,check;
    ImageView image;
    private BroadcastReceiver broadcastReceiver;
    Toolbar toolbar;
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
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
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

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
//        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        date.setText(strDate+"   "+strTime);
        title.setText(strTitle);
        message.setText(strMessage);
        Glide.with(NotificationDetailActivity.this).load(strImage).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(image);
        database.getReference().child("Users").child(auth.getUid()).child("Notification").child("mini notification")
                        .child(notificationId).child("seen").setValue(true);

        exit.setOnClickListener(v -> {
            startActivity(new Intent(NotificationDetailActivity.this, MainActivity.class));
            finish();
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.tolProfile){
            Intent intent = new Intent(NotificationDetailActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(NotificationDetailActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(NotificationDetailActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}