package com.echatti.hatti.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.echatti.hatti.R;
import com.echatti.hatti.activity.AboutActivity;
import com.echatti.hatti.activity.ContactActivity;
import com.echatti.hatti.models.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileSetupActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button update;
    TextView name,gmail,phone,dob,address,city,state,gender,hattiId;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    ImageView profileImg,shopImg;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        update = findViewById(R.id.btnUpdateProfile);
        name = findViewById(R.id.etNameForm);
        gmail = findViewById(R.id.etGmailForm);
        phone = findViewById(R.id.etPhoneForm);
        dob = findViewById(R.id.etDobForm);
        address = findViewById(R.id.etAddressForm);
        city= findViewById(R.id.etCityForm);
        state = findViewById(R.id.etStateForm);
        gender = findViewById(R.id.etGenderForm);
        hattiId = findViewById(R.id.etHattiIdForm);
        progressBar = findViewById(R.id.pbProfileSetupProgressBar);
        linearLayout = findViewById(R.id.llProfileSetupActivityLayout);
        profileImg = findViewById(R.id.ivProfileSetupProfileImg);
        shopImg = findViewById(R.id.ivProfileSetupShopImg);

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        database.getReference().child("Profiles").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfileModel model = snapshot.getValue(ProfileModel.class);
                        Glide.with(ProfileSetupActivity.this).load(model.getProfilePhoto()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(profileImg);
                        Glide.with(ProfileSetupActivity.this).load(model.getShopPhoto()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(shopImg);
                        name.setText(model.getName());
                        address.setText(model.getAddress());
                        state.setText(model.getState());
                        phone.setText(model.getPhoneNo());
                        city.setText(model.getCity());
                        dob.setText(model.getDob());
                        gmail.setText(model.getGmail());
                        gender.setText(model.getGender());
                        hattiId.setText(model.getHattiId());
                        linearLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileSetupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        update.setOnClickListener(v -> {
            Dialog dialog = new Dialog(ProfileSetupActivity.this);
            dialog.setContentView(R.layout.call_dialog_box);
            dialog.show();
            Button button = dialog.findViewById(R.id.btnCallDialogBoxButton);
            TextView msg = dialog.findViewById(R.id.tvCallDialogBoxMsg);
            msg.setText("Update Profile");
            button.setOnClickListener(v1 -> {
                dialog.dismiss();
                database.getReference().child("Hatti").child("contact detail").child("phoneNo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phoneNumber=snapshot.getValue(String.class);
                        if (Build.VERSION.SDK_INT > 22) {

                            if (ActivityCompat.checkSelfPermission(ProfileSetupActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions((Activity) ProfileSetupActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                                return;
                            }
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            assert phoneNumber != null;
                            callIntent.setData(Uri.parse("tel:" + phoneNumber.trim()));
                            startActivity(callIntent);
                        } else {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            assert phoneNumber != null;
                            callIntent.setData(Uri.parse("tel:" + phoneNumber.trim()));
                            startActivity(callIntent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileSetupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            });
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
            Intent intent = new Intent(ProfileSetupActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(ProfileSetupActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(ProfileSetupActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}