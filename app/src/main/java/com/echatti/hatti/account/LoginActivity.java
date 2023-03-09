package com.echatti.hatti.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.echatti.hatti.MainActivity;
import com.echatti.hatti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText hattiId,password;
    boolean passwordVisible=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialize
        auth = FirebaseAuth.getInstance();
        Button login = findViewById(R.id.btnLogin);
        hattiId = findViewById(R.id.etHattiId);
        password = findViewById(R.id.etPassword);
        TextView createAccount = findViewById(R.id.tvCreateAccount);
        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_24,0);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        password.setOnTouchListener((view, motionEvent) -> {
            final int Right=2;
            if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                if(motionEvent.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                    int selection = password.getSelectionEnd();
                    if(passwordVisible){
                        password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_off_24,0);
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible=false;
                    }
                    else {
                        password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_24,0);
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible=true;
                    }
                    password.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        login.setOnClickListener(v -> {
            String sId,sPassword;
            sId = hattiId.getText().toString();
            sPassword = password.getText().toString();
            if (sId.isEmpty()){
                hattiId.setError("required");
            }
            else if(sPassword.isEmpty()){
                password.setError("required");
            }
            else {
                auth.signInWithEmailAndPassword(sId.trim()+"@gmail.com",sPassword.trim())
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(task1 -> {
                                            if (!task1.isSuccessful()) {
                                                return;
                                            }
                                            String token = task1.getResult();
                                            database.getReference().child("Profiles").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("token").setValue(token)
                                                    .addOnCompleteListener(task11 -> {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.putExtra("activity","Next");
                                                        startActivity(intent);
                                                        finish();
                                                    });
                                        });
                            }
                            else {
                                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        createAccount.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();

        }

    }
}