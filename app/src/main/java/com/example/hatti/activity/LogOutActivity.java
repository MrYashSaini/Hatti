package com.example.hatti.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.example.hatti.R;
import com.example.hatti.account.LoginActivity;
import com.example.hatti.internet.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;

public class LogOutActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        auth.signOut();
        startActivity(new Intent(LogOutActivity.this, LoginActivity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}