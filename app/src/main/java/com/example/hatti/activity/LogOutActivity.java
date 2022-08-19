package com.example.hatti.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hatti.R;
import com.example.hatti.account.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        startActivity(new Intent(LogOutActivity.this, LoginActivity.class));
    }
}