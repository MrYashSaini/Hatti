package com.example.hatti.internet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;

import com.example.hatti.databinding.CheckInternetDialogBinding;

public class NetworkBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isNetworkConnected(context)){
            CheckInternetDialogBinding binding = CheckInternetDialogBinding.inflate(LayoutInflater.from(context));
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(binding.getRoot());
            Dialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
            binding.btnRetry.setOnClickListener(v -> {
                if (isNetworkConnected(context)){
                    dialog.dismiss();
                }
            });
        }
    }
    private boolean isNetworkConnected(Context context){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
