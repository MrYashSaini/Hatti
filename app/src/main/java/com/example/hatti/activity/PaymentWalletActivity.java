package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hatti.R;
import com.example.hatti.adapter.PaymentAdapter;
import com.example.hatti.models.PaymentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentWalletActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<PaymentModel> list = new ArrayList<>();
    RecyclerView paymentRecyclerView;
    TextView tvTotalAmount,tvTotalPayAmount,tvDueAmount,tvDueOrderAmount;
    int totalAmount =0,totalPayAmount=0,totalDueAmount=0,dueOrderPayment=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_wallet);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        paymentRecyclerView = findViewById(R.id.rvPaymentList);
        tvTotalAmount = findViewById(R.id.tvPaymentFullTotalAmount);
        tvTotalPayAmount  = findViewById(R.id.tvPaymentFullPayAmount);
        tvDueAmount = findViewById(R.id.tvPaymentFullDueAmount);
        tvDueOrderAmount = findViewById(R.id.tvPaymentFullDueOrderPayment);


        PaymentAdapter adapter = new PaymentAdapter(PaymentWalletActivity.this,list);
        paymentRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentWalletActivity.this);
        paymentRecyclerView.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users").child(auth.getUid()).child("Payment").child("payment list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            PaymentModel model = snapshot1.getValue(PaymentModel.class);
                            Integer strAmount = Integer.valueOf(model.getAmount());
                            int intAmount = strAmount.intValue();

                            Integer strPay = Integer.valueOf(model.getPay());
                            int intPay = strPay.intValue();

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

                        adapter.notifyDataSetChanged();
                        tvTotalAmount.setText(totalAmount+"");
                        tvTotalPayAmount.setText(totalPayAmount+"");
                        tvDueAmount.setText(totalDueAmount+"");
                        tvDueOrderAmount.setText(dueOrderPayment+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}