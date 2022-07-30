package com.example.hatti.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button login;
    EditText hattiId,password;
    TextView createAccount;
    boolean passwordVisible=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialize
        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.btnLogin);
        hattiId = findViewById(R.id.etHattiId);
        password = findViewById(R.id.etPassword);
        createAccount = findViewById(R.id.tvCreateAccount);
        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    auth.signInWithEmailAndPassword(sId+"@gmail.com",sPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(LoginActivity.this,ProfileSetupActivity.class));
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
            }
        });

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,ProfileSetupActivity.class));

        }

    }
}