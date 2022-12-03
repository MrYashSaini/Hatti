package com.example.hatti.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import com.example.hatti.R;
import com.example.hatti.adapter.SearchAdapter;
import com.example.hatti.internet.NetworkBroadcast;
import com.example.hatti.models.SearchModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<SearchModel> list = new ArrayList<>();
    SearchAdapter searchAdapter;
    private BroadcastReceiver broadcastReceiver;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        database = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.rvSearchShowProduct);
        searchView = findViewById(R.id.svSearchOption);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
//        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

//        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));



        searchAdapter = new SearchAdapter(SearchActivity.this,list);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        database.getReference().child("categorys").child("all product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    SearchModel model = snapshot1.getValue(SearchModel.class);
                    list.add(model);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        list.add(new SearchModel("image","namkeen","Rice","1001"));
//        list.add(new SearchModel("image","rice","Rice","1001"));
//        list.add(new SearchModel("image","biscut","Rice","1001"));
//        list.add(new SearchModel("image","parleg","Rice","1001"));
//        list.add(new SearchModel("image","good day","Rice","1001"));
//        list.add(new SearchModel("image","candy","Rice","1001"));

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.requestFocus();
            }
        });
    }

    private void filterList(String newText) {
        ArrayList<SearchModel> filteredList = new ArrayList<>();
        for(SearchModel item : list){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (!filteredList.isEmpty()){
            searchAdapter.setFilteredList(filteredList);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}