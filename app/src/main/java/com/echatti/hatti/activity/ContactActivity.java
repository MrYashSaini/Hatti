package com.echatti.hatti.activity;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.echatti.hatti.R;
import com.echatti.hatti.account.LoginActivity;
import com.echatti.hatti.account.ProfileSetupActivity;
import com.echatti.hatti.internet.NetworkBroadcast;
import com.echatti.hatti.models.AboutModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ContactActivity extends AppCompatActivity {
    TextView gmail,facebook,instagram,phoneNumber,detail;
    Button back,phone;
    FirebaseDatabase database;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    String facebookLink,instagramLink;

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
        detail = findViewById(R.id.tvContactDetail);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        DocumentReference docRef = db.collection("Contact").document("contact");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                AboutModel model = document.toObject(AboutModel.class);
                facebook.setText(model.getFacebook());
                instagram.setText(model.getInstagram());
                gmail.setText(model.getGmail());
                phoneNumber.setText(model.getPhoneNo());
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
        database.getReference().child("Hatti").child("Hatti Details").child("contactDetail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detail.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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