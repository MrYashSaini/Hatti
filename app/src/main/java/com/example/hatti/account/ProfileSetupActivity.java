package com.example.hatti.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hatti.MainActivity;
import com.example.hatti.R;
import com.example.hatti.models.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfileSetupActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button next;
    EditText name,gmail,phone,dob,address,city,state;
    RadioButton rbMale,rbFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        next = findViewById(R.id.btnNext);
        name = findViewById(R.id.etNameForm);
        gmail = findViewById(R.id.etGmailForm);
        phone = findViewById(R.id.etPhoneForm);
        dob = findViewById(R.id.etDobForm);
        address = findViewById(R.id.etAddressForm);
        city= findViewById(R.id.etCityForm);
        state = findViewById(R.id.etStateForm);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);

        String btnName = getIntent().getStringExtra("activity");
        if (btnName!="Next"){
            next.setText("update");
        }
        else {
            next.setText(btnName);
        }
//        startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sName,sGmail,sPhone,sDob,sAddress,sCity,sState,sGender = null;
                sName = name.getText().toString();
                sGmail= gmail.getText().toString();
                sPhone= phone.getText().toString();
                sAddress = address.getText().toString();
                sCity = city.getText().toString();
                sState = state.getText().toString();
                sDob = dob.getText().toString();

                if(sName.isEmpty()){
                    name.setError("required");
                }

                else if(sGmail.isEmpty()){
                    gmail.setError("required");
                }
                else if(sPhone.isEmpty()){
                    phone.setError("required");
                }
                else if(sState.isEmpty()){
                    state.setError("required");
                }
                else if(sCity.isEmpty()){
                    city.setError("required");
                }
                else if(sAddress.isEmpty()){
                    address.setError("required");
                }
                else if(sDob.isEmpty()){
                    dob.setError("required");
                }
                else if(!rbMale.isChecked() && !rbFemale.isChecked()){
                    Toast.makeText(ProfileSetupActivity.this, "select your gender", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(rbMale.isChecked()){
                        sGender = "Male";
                    }
                    if (rbFemale.isChecked()){
                        sGender = "Female";
                    }
                    ProfileModel profileModel = new ProfileModel();
                    profileModel.setName(sName);
                    profileModel.setAddress(sAddress);
                    profileModel.setCity(sCity);
                    profileModel.setDob(sDob);
                    profileModel.setPhone(sPhone);
                    profileModel.setGender(sGender);
                    profileModel.setState(sState);
                    profileModel.setGmail(sGmail);

                    database.getReference().child("users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("profile").setValue(profileModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(ProfileSetupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}