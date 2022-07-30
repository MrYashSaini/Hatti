package com.example.hatti.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.models.AboutModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccountActivity extends AppCompatActivity {
    TextView gmail,facebook,instagram,phoneNumber;
    Button back,phone;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        back = findViewById(R.id.btnBack);
        gmail = findViewById(R.id.tvGamilLink);
        facebook = findViewById(R.id.tvFacebookLink);
        instagram = findViewById(R.id.tvInstagramLink);
        phone = findViewById(R.id.btnPhoneCall);
        phoneNumber = findViewById(R.id.tvPhoneNo);
        database = FirebaseDatabase.getInstance();

        database.getReference().child("hatti").child("about").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AboutModel aboutModel = snapshot.getValue(AboutModel.class);
                facebook.setText(aboutModel.getFacebook());
                instagram.setText(aboutModel.getInstagram());
                gmail.setText(aboutModel.getGmail());
                phoneNumber.setText(aboutModel.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {

                    if (ActivityCompat.checkSelfPermission(CreateAccountActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(CreateAccountActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                        return;
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber.getText().toString().trim()));
                    startActivity(callIntent);
                } else {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber.getText().toString().trim()));
                    startActivity(callIntent);
                }
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email= new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:"+gmail.getText().toString())); // your.email@gmail.com
                email.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                email.putExtra(Intent.EXTRA_TEXT, "My Email message");
                startActivity(email);
            }
        });

        facebook.setOnClickListener(v -> Linkify.addLinks(facebook,Linkify.WEB_URLS));
        instagram.setOnClickListener(v -> Linkify.addLinks(instagram,Linkify.WEB_URLS));

        back.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this,LoginActivity.class)));




    }
}