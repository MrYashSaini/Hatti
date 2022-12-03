package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.MainActivity;
import com.example.hatti.R;
import com.example.hatti.account.LoginActivity;
import com.example.hatti.account.ProfileSetupActivity;
import com.example.hatti.fragments.CartFragment;
import com.example.hatti.internet.NetworkBroadcast;
import com.example.hatti.models.AboutModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactActivity extends AppCompatActivity {
    TextView gmail,facebook,instagram,phoneNumber;
    Button back,phone;
    FirebaseDatabase database;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        back = findViewById(R.id.btnBackForContact);
        gmail = findViewById(R.id.tvGamilLinkForContact);
        facebook = findViewById(R.id.tvFacebookLinkForContact);
        instagram = findViewById(R.id.tvInstagramLinkForContact);
        phone = findViewById(R.id.btnPhoneCallForContact);
        phoneNumber = findViewById(R.id.tvPhoneNoForContact);
        database = FirebaseDatabase.getInstance();

        //        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        database.getReference().child("Hatti").child("contact detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AboutModel aboutModel = snapshot.getValue(AboutModel.class);
                assert aboutModel != null;
                facebook.setText(aboutModel.getFacebook());
                instagram.setText(aboutModel.getInstagram());
                gmail.setText(aboutModel.getGmail());
                phoneNumber.setText(aboutModel.getPhoneNo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContactActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        phone.setOnClickListener(v -> {
            Dialog dialog = new Dialog(ContactActivity.this);
            dialog.setContentView(R.layout.call_dialog_box);
            dialog.show();
            Button button = dialog.findViewById(R.id.btnCallDialogBoxButton);
            TextView msg = dialog.findViewById(R.id.tvCallDialogBoxMsg);
            msg.setText("Call to Suppler");
            button.setOnClickListener(v1 -> {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT > 22) {

                    if (ActivityCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

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
            });

        });

        gmail.setOnClickListener(v -> {
            Intent email= new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:"+gmail.getText().toString())); // your.email@gmail.com
            email.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            email.putExtra(Intent.EXTRA_TEXT, "My Email message");
            startActivity(email);
        });

        facebook.setOnClickListener(v -> Linkify.addLinks(facebook,Linkify.WEB_URLS));
        instagram.setOnClickListener(v -> Linkify.addLinks(instagram,Linkify.WEB_URLS));

        back.setOnClickListener(v -> startActivity(new Intent(ContactActivity.this, LoginActivity.class)));

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
            Intent intent = new Intent(ContactActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(ContactActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(ContactActivity.this, AboutActivity.class);
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