package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
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

import com.example.hatti.R;
import com.example.hatti.account.ProfileSetupActivity;
import com.example.hatti.adapter.PaymentAdapter;
import com.example.hatti.internet.NetworkBroadcast;
import com.example.hatti.models.PaymentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class PaymentWalletActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<PaymentModel> list = new ArrayList<>();
    RecyclerView paymentRecyclerView;
    TextView tvTotalAmount,tvTotalPayAmount,tvDueAmount,tvDueOrderAmount,call;
    int totalAmount =0,totalPayAmount=0,totalDueAmount=0,dueOrderPayment=0;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_wallet);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        paymentRecyclerView = findViewById(R.id.rvPaymentList);
        tvTotalAmount = findViewById(R.id.tvPaymentFullTotalAmount);
        tvTotalPayAmount  = findViewById(R.id.tvPaymentFullPayAmount);
        tvDueAmount = findViewById(R.id.tvPaymentFullDueAmount);
        tvDueOrderAmount = findViewById(R.id.tvPaymentFullDueOrderPayment);
        call = findViewById(R.id.tvPaymentCallForPayment);
        progressBar = findViewById(R.id.pbPaymentWalletProgressBar);
        linearLayout = findViewById(R.id.llPaymentWalletActivityLayout);

//        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        PaymentAdapter adapter = new PaymentAdapter(PaymentWalletActivity.this,list);
        paymentRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentWalletActivity.this);
        paymentRecyclerView.setLayoutManager(linearLayoutManager);
        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Payment").child("payment list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            PaymentModel model = snapshot1.getValue(PaymentModel.class);
                            assert model != null;
                            int intAmount = Integer.parseInt(model.getAmount());

                            int intPay = Integer.parseInt(model.getPay());

                            int due = intAmount-intPay;
                            model.setDue(due+"");
                            totalAmount += intAmount;
                            totalDueAmount += due;
                            totalPayAmount += intPay;
                            if(due != 0){
                                dueOrderPayment += 1;
                            }
                            list.add(model);
                        }
                        Collections.reverse(list);
                        adapter.notifyDataSetChanged();
                        tvTotalAmount.setText(totalAmount+"");
                        tvTotalPayAmount.setText(totalPayAmount+"");
                        tvDueAmount.setText(totalDueAmount+"");
                        tvDueOrderAmount.setText(dueOrderPayment+"");
                        linearLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PaymentWalletActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        call.setOnClickListener(v -> {
            Dialog dialog = new Dialog(PaymentWalletActivity.this);
            dialog.setContentView(R.layout.call_dialog_box);
            dialog.show();
            Button button = dialog.findViewById(R.id.btnCallDialogBoxButton);
            TextView msg = dialog.findViewById(R.id.tvCallDialogBoxMsg);
            msg.setText("Call to Suppler for payment");
            button.setOnClickListener(v1 -> {
                dialog.dismiss();
                database.getReference().child("Hatti").child("contact detail").child("phoneNo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phoneNumber=snapshot.getValue(String.class);
                        if (Build.VERSION.SDK_INT > 22) {

                            if (ActivityCompat.checkSelfPermission(PaymentWalletActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions((Activity) PaymentWalletActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

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
                        Toast.makeText(PaymentWalletActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(PaymentWalletActivity.this, ProfileSetupActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolContact) {
            Intent intent = new Intent(PaymentWalletActivity.this, ContactActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.tolAbout) {
            Intent intent = new Intent(PaymentWalletActivity.this, AboutActivity.class);
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