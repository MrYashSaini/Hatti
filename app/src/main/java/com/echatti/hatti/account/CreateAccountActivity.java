package com.echatti.hatti.account;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echatti.hatti.R;
import com.echatti.hatti.models.AboutModel;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccountActivity extends AppCompatActivity {
    TextView gmail,facebook,instagram,phoneNumber;
    Button back,phone;
    FirebaseDatabase database;
    ProgressBar progressBar;

    LinearLayout linearLayout;
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
        progressBar = findViewById(R.id.pbCreateAccountProgressBar);
        linearLayout = findViewById(R.id.llCreateAccountActivityLayout);
        database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        phone.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT > 22) {

                if (ActivityCompat.checkSelfPermission(CreateAccountActivity.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

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
        });

        gmail.setOnClickListener(v -> {
            Intent email= new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:"+gmail.getText().toString()));
            email.putExtra(Intent.EXTRA_SUBJECT, "mySubject");
            email.putExtra(Intent.EXTRA_TEXT, "My Email message");
            startActivity(email);
        });

        facebook.setOnClickListener(v -> Linkify.addLinks(facebook,Linkify.WEB_URLS));
        instagram.setOnClickListener(v -> Linkify.addLinks(instagram,Linkify.WEB_URLS));

        back.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this,LoginActivity.class)));

        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

    }
}