package com.echatti.hatti;


import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.echatti.hatti.activity.SearchActivity;
import com.echatti.hatti.databinding.ActivityMainBinding;
import com.echatti.hatti.fragments.CartFragment;
import com.echatti.hatti.internet.NetworkBroadcast;
import com.echatti.hatti.models.ProfileModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    Toolbar toolbar;
    FirebaseDatabase database;
    FirebaseAuth auth;
    int notificationNo = 0;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupNavigation();
        toolbar = findViewById(R.id.toolbar);
        database =FirebaseDatabase.getInstance();
//        check internet
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        auth = FirebaseAuth.getInstance();
        toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu,null);
            toolbar.setNavigationIcon(d);
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
//
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_category, R.id.nav_order,R.id.nav_notification)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        NavigationUI.setupWithNavController(binding.appBarMain.bottomNavView, navController);

        View headerView = navigationView.getHeaderView(0);
        ImageView profileImg = headerView.findViewById(R.id.ivNavHeaderProfileImg);
        ImageView shopImg = headerView.findViewById(R.id.ivNavHeaderShopImg);
        TextView clientName = headerView.findViewById(R.id.tvUserName);
        TextView clientId = headerView.findViewById(R.id.tvHattiId);

//        binding.appBarMain.bottomNavView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#FF08E2FE")));
//        binding.appBarMain.bottomNavView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#FF525151")));
//        token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    database.getReference().child("Profiles").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("token").setValue(token);
                });
//      get profile data and set images
        database.getReference().child("Profiles").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel model = snapshot.getValue(ProfileModel.class);
                try{
                    Glide.with(MainActivity.this).load(model.getProfilePhoto()).apply(new RequestOptions().placeholder(R.drawable.user)).into(profileImg);
                    Glide.with(MainActivity.this).load(model.getShopPhoto()).apply(new RequestOptions().placeholder(R.drawable.side_nav_bar)).into(shopImg);
                    clientName.setText(model.getName());
                    clientId.setText(model.getHattiId());
                    notificationNo = model.getNotificationNo();
                }
                catch (Exception ignored){
                }

                if (notificationNo>0){
                    BadgeDrawable badgeNotification = binding.appBarMain.bottomNavView.getOrCreateBadge(R.id.nav_notification);
                    badgeNotification.setNumber(notificationNo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        binding.appBarMain.bottomNavView.setOnNavigationItemSelectedListener(item -> {
//            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            switch (item.getItemId()){
//                case R.id.nav_notification:
//                    if (notificationNo>0){
//                        BadgeDrawable badgeNotification = binding.appBarMain.bottomNavView.getBadge(R.id.nav_notification);
//                        badgeNotification.clearNumber();
//                        badgeNotification.setVisible(false);
//
//
//                    }
//
//                    NotificationFragment notificationFragment = new NotificationFragment();
//                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main,notificationFragment);
//                    fragmentTransaction.commit();
//                    toolbar.post(() -> {
//                        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu,null);
//                        toolbar.setNavigationIcon(d);
//                    });
//                    break;
//                case R.id.nav_home:
//
//                    HomeFragment homeFragment = new HomeFragment();
//                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, homeFragment);
//                    fragmentTransaction.commit();
//                    toolbar.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu,null);
//                            toolbar.setNavigationIcon(d);
//                        }
//                    });
//                    break;
//                case R.id.nav_category:
//                    CategoryFragment categoryFragment = new CategoryFragment();
//                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, categoryFragment);
//                    fragmentTransaction.commit();
//                    toolbar.post(() -> {
//                        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu,null);
//                        toolbar.setNavigationIcon(d);
//                    });
//                    break;
//                case R.id.nav_order:
//                    OrderFragment orderFragment = new OrderFragment();
//                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, orderFragment);
//                    fragmentTransaction.commit();
//                    toolbar.post(() -> {
//                        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu,null);
//                        toolbar.setNavigationIcon(d);
//                    });
//                    break;
//                default:
//                    HomeFragment homeFragment2 = new HomeFragment();
//                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, homeFragment2);
//                    fragmentTransaction.commit();
//                    toolbar.post(() -> {
//                        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu,null);
//                        toolbar.setNavigationIcon(d);
//                    });
//
//            }
//            return false;
//        });

            }
    private void setupNavigation() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_category, R.id.nav_order, R.id.nav_notification)
                .setOpenableLayout(drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_addtocart){
            CartFragment fragment = new CartFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null).commit();

            return true;
        } else if (id==R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}