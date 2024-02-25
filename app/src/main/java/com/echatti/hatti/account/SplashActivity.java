package com.echatti.hatti.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.echatti.hatti.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Thread thread = new Thread(() -> {
            try{
                Thread.sleep(1500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        });thread.start();
    }
}