package com.echatti.hatti.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.echatti.hatti.MainActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.echatti.hatti.R;
import com.echatti.hatti.models.NewProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class NewCreateAccountActivity extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    EditText name,id,password,gmail,address,phoneNo,city,state,dob,gender;
    ImageView shopImg,profileImg,addProfileImg,addShopImg;
    Button createAccountButton;
    String profileImgUri= "",shopImgUri="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_create_account_activity);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        name = findViewById(R.id.etCreateAccountName);
        id = findViewById(R.id.etCreateAccountId);
        password = findViewById(R.id.etCreateAccountPassword);
        gmail = findViewById(R.id.etCreateAccountGmail);
        address = findViewById(R.id.etCreateAccountAddress);
        phoneNo = findViewById(R.id.etCreateAccountPhoneNo);
        city = findViewById(R.id.etCreateAccountCity);
        state = findViewById(R.id.etCreateAccountState);
        dob = findViewById(R.id.etCreateAccountDob);
        gender = findViewById(R.id.etCreateAccountGender);
        shopImg = findViewById(R.id.ivCreateAccountShopPhoto);
        profileImg = findViewById(R.id.ivCreateAccountProfilePhoto);
        addShopImg = findViewById(R.id.ivCreateAccountAddShopImg);
        addProfileImg = findViewById(R.id.ivCreateAccountAddClientimg);
        createAccountButton = findViewById(R.id.btnCreateAccountButton);
        auth = FirebaseAuth.getInstance();
        addProfileImg.setOnClickListener(v -> ImagePicker.Companion.with(NewCreateAccountActivity.this)
                .crop()
                .cropSquare()
                .start(1));

        addShopImg.setOnClickListener(v -> ImagePicker.Companion.with(NewCreateAccountActivity.this)
                .crop()
                .start(2));
//        add profile in firebase
        createAccountButton.setOnClickListener(v -> {
            String strName,strId,strAddress,strCity,strState,strDob,strPassword,strGender,strPhoneNo,strGmail;
            strName = name.getText().toString();
            strId = id.getText().toString().trim();
            strCity = city.getText().toString();
            strAddress = address.getText().toString();
            strState = state.getText().toString();
            strDob = dob.getText().toString();
            strPassword = password.getText().toString();
            strGender = gender.getText().toString();
            strPhoneNo = phoneNo.getText().toString();
            strGmail = gmail.getText().toString();
            if (profileImgUri.isEmpty() || shopImgUri.isEmpty()){
                Toast.makeText(NewCreateAccountActivity.this, "select Image", Toast.LENGTH_SHORT).show();
            }
            else if(strName.isEmpty() || strId.isEmpty() || strCity.isEmpty() || strAddress.isEmpty() || strState.isEmpty() || strDob.isEmpty() || strPassword.isEmpty() || strGender.isEmpty() || strPhoneNo.isEmpty() || strGmail.isEmpty() ){
                Toast.makeText(NewCreateAccountActivity.this, "fill the form", Toast.LENGTH_SHORT).show();
            }
            else {
                ProgressDialog progressDialog = new ProgressDialog(NewCreateAccountActivity.this);
                progressDialog.setTitle("create Account");
                progressDialog.setMessage("Loading..");
                progressDialog.show();
//                    upload img on firebase
                StorageReference storageReference = storage.getReference().child("Customer Photo");
                storageReference.child(strId+"profileImg").putFile(Uri.parse(profileImgUri))
                        .addOnSuccessListener(taskSnapshot -> storageReference.child(strId+"profileImg").getDownloadUrl().addOnSuccessListener(uri -> {
                            profileImgUri = uri.toString();
                            storageReference.child(strId+"shopImg").putFile(Uri.parse(shopImgUri))
                                    .addOnSuccessListener(taskSnapshot1 -> storageReference.child(strId+"shopImg").getDownloadUrl().addOnSuccessListener(uri1 -> {
                                        shopImgUri = uri1.toString();
                                        auth.createUserWithEmailAndPassword(id.getText().toString().trim()+"@gmail.com",password.getText().toString())
                                                .addOnCompleteListener(task -> {
                                                    if(task.isSuccessful()){
                                                        NewProfileModel model = new NewProfileModel();
                                                        model.setName(strName);
                                                        model.setHattiId(strId);
                                                        model.setAddress(strAddress);
                                                        model.setCity(strCity);
                                                        model.setState(strState);
                                                        model.setDob(strDob);
                                                        model.setPassword(strPassword);
                                                        model.setGender(strGender);
                                                        model.setPhoneNo(strPhoneNo);
                                                        model.setAuthId(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                                                        model.setProfilePhoto(profileImgUri);
                                                        model.setShopPhoto(shopImgUri);
                                                        model.setGmail(strGmail);
                                                        model.setNotificationId(1000);
                                                        model.setOrderId(1000);
                                                        model.setNotificationNo(0);

                                                        database.getReference().child("Profiles").child(auth.getCurrentUser().getUid()).setValue(model)
                                                                .addOnCompleteListener(task1 -> {
                                                                    if (task1.isSuccessful()){
                                                                        name.setText("");
                                                                        id.setText("");
                                                                        gmail.setText("");
                                                                        address.setText("");
                                                                        phoneNo.setText("");
                                                                        password.setText("");
                                                                        city.setText("");
                                                                        state.setText("");
                                                                        dob.setText("");
                                                                        gender.setText("");
                                                                        profileImgUri="";
                                                                        shopImgUri="";
                                                                        profileImg.setImageResource(R.drawable.user);
                                                                        shopImg.setImageResource(R.drawable.placeholder);
                                                                        progressDialog.dismiss();
                                                                        startActivity(new Intent(NewCreateAccountActivity.this, MainActivity.class));
                                                                        finish();

                                                                    }
                                                                    else {
                                                                        Toast.makeText(NewCreateAccountActivity.this, "check your internet connection", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                    }));
                        }));
            }
        });
    }
// get img url
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            assert data != null;
            Uri uri = data.getData();
            
            try {
                profileImg.setImageURI(uri);
                profileImgUri = uri.toString();
            } catch (Exception e) {
                Toast.makeText(this, "select photo", Toast.LENGTH_SHORT).show();
            }

        }
        else if ( requestCode ==2){
            assert data != null;
            Uri uri = data.getData();
            

            try {
                shopImg.setImageURI(uri);
                shopImgUri = uri.toString();
            } catch (Exception e) {
                Toast.makeText(this, "select photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}