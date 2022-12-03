package com.example.hatti.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatti.R;
import com.example.hatti.adapter.CategoryMenuAdapter;
import com.example.hatti.adapter.HorizontalProductScrollAdapter;
import com.example.hatti.adapter.SliderAdapter;
import com.example.hatti.models.CartModel;
import com.example.hatti.models.HomeMenuModel;
import com.example.hatti.models.HorizontalProductScrollModel;
import com.example.hatti.models.SliderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    FirebaseDatabase database;
    ArrayList<HomeMenuModel> categoryMenuList;
    // for banner
    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;
    FirebaseFirestore firestore;
    private Timer timer;
    ProgressBar progressBar;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Category Menu
        RecyclerView categoryRecyclerView = view.findViewById(R.id.rvCategoryMenu);
        categoryMenuList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.pbHomeProgressBar);

        bannerSliderViewPager = view.findViewById(R.id.vpBannerSlieder);
        sliderModelList = new ArrayList<>();

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper();
                }
            }
        };

        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
        startBannerSlideShow();
        bannerSliderViewPager.setOnTouchListener((v, event) -> {
            pageLooper();
            stopBannerSlideShow();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                startBannerSlideShow();
            }
            return false;
        });
        firestore.collection("BANNERS").orderBy("index").get()
                .addOnCompleteListener(task -> {
                    sliderModelList.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            sliderModelList.add(new SliderModel(Objects.requireNonNull(documentSnapshot.get("icon")).toString()));
                            sliderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        CategoryMenuAdapter categoryMenuAdapter = new CategoryMenuAdapter(categoryMenuList, getContext());
        categoryRecyclerView.setAdapter(categoryMenuAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Hatti").child("Home Menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryMenuList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    HomeMenuModel model = snapshot1.getValue(HomeMenuModel.class);
                    categoryMenuList.add(model);
                    categoryMenuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //---------------------------------------------
        // horizontal product view
        // for horizontal product view
        TextView horizontalLayoutTitle = view.findViewById(R.id.horizontalScrollViewTitle);
        horizontalLayoutTitle.setText(R.string.recommended);
        Button btnViewAll = view.findViewById(R.id.btnHorizontalScrollViewButton);
        btnViewAll.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("Users").child(Objects.requireNonNull(auth.getUid())).child("CategoryShow").child("category").setValue("recommended");
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            CategoryItemsFragment fragment = new CategoryItemsFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).addToBackStack(null).commit();

        });
//        contact
        RecyclerView rvProductLayout = view.findViewById(R.id.rvHorizontalView);
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();

        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList,getContext());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvProductLayout.setLayoutManager(linearLayoutManager);
        rvProductLayout.setAdapter(horizontalProductScrollAdapter);
        database.getReference().child("Hatti").child("recommended products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                horizontalProductScrollModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartModel product = dataSnapshot.getValue(CartModel.class);
                    database.getReference().child("categorys").child("product category").child(Objects.requireNonNull(product).getCategory()).child(product.getProductId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HorizontalProductScrollModel productDetail = snapshot.getValue(HorizontalProductScrollModel.class);
                                    horizontalProductScrollModelList.add(productDetail);
                                    horizontalProductScrollAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

                LinearLayout linearLayout = view.findViewById(R.id.homeFragmentLayout);
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                horizontalProductScrollAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//--------------------------------------------
//2 horizontal scroll view
//        TextView horizontalLayoutTitle2 = view.findViewById(R.id.horizontalScrollViewTitle2);
//        horizontalLayoutTitle2.setText("Top Products");
//        Button btnViewAll2 = view.findViewById(R.id.btnHorizontalScrollViewButton2);
//        btnViewAll2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference();
//                myRef.child("Users").child(auth.getUid()).child("CategoryShow").child("category").setValue("Top product");
//                AppCompatActivity activity = (AppCompatActivity)v.getContext();
//                CategoryItemsFragment fragment = new CategoryItemsFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack(null).commit();
//            }
//        });
//
//        RecyclerView rvSecondHV = view.findViewById(R.id.rvHorizontalView2);
//        ArrayList<categoryProductModel> models = new ArrayList<>();
//
//        TopProductAdapter topProductAdapter = new TopProductAdapter(models,getContext());
//        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
//        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rvSecondHV.setLayoutManager(linearLayoutManager2);
//        rvSecondHV.setAdapter(topProductAdapter);
//        database.getReference().child("Hatti").child("top products").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                models.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    CartModel product = dataSnapshot.getValue(CartModel.class);
//                    database.getReference().child("categorys").child("product category").child(product.getCategory()).child(product.getProductId())
//                            .addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    categoryProductModel products = snapshot.getValue(categoryProductModel.class);
//                                    models.add(products);
//                                    topProductAdapter.notifyDataSetChanged();
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                }
//                topProductAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//-----------------------

        //view Pager


        return view;
    }

    // show banner image in loop format
    private void pageLooper() {
        if (currentPage == sliderModelList.size() - 2) {
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage, true);
        }
        if (currentPage == 1) {
            currentPage = sliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem(currentPage, true);
        }
    }

    private void startBannerSlideShow() {
        Handler handler = new Handler();
        Runnable update = () -> {
            if (currentPage >= sliderModelList.size()) {
                currentPage = 1;
            }
            bannerSliderViewPager.setCurrentItem(currentPage++, true);
        };
        timer = new Timer();
        long PERIOD_TIME = 3000;
        long DELAY_TIME = 3000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }

    private void stopBannerSlideShow() {
        timer.cancel();
    }
}