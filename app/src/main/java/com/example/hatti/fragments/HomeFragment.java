package com.example.hatti.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hatti.R;
import com.example.hatti.adapter.CategoryMenuAdapter;
import com.example.hatti.adapter.HorizontalProductScrollAdapter;
import com.example.hatti.adapter.SliderAdapter;
import com.example.hatti.models.CategoryMenuModel;
import com.example.hatti.models.HorizontalProductScrollModel;
import com.example.hatti.models.SliderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    // for category
    ArrayList<CategoryMenuModel> categoryMenuList = new ArrayList<>();
    // for banner
    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;
    private Timer timer;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    // for horizontal product view
    private TextView horizontalLayoutTitle;
    private Button btnViewAll;
    private RecyclerView rvProductLayout;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        // Category Menu
        RecyclerView categoryRecyclerView = view.findViewById(R.id.rvCategoryMenu);

        CategoryMenuAdapter categoryMenuAdapter = new CategoryMenuAdapter(categoryMenuList, getContext());
        categoryRecyclerView.setAdapter(categoryMenuAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryMenuList.add(new CategoryMenuModel("link","All Item"));
        categoryMenuList.add(new CategoryMenuModel("link","Food and Oil"));
        categoryMenuList.add(new CategoryMenuModel("link","Beverages"));
        categoryMenuList.add(new CategoryMenuModel("link","Snacks"));
        categoryMenuList.add(new CategoryMenuModel("link","Beauty and Hygiene"));
        categoryMenuList.add(new CategoryMenuModel("link","Masala"));
        categoryMenuList.add(new CategoryMenuModel("link","Cleaning and Household"));
        categoryMenuAdapter.notifyDataSetChanged();

        //---------------------------------------------
        // horizontal product view
        horizontalLayoutTitle = view.findViewById(R.id.horizontalScrollViewTitle);
        btnViewAll = view.findViewById(R.id.btnHorizontalScrollViewButton);
        rvProductLayout = view.findViewById(R.id.rvHorizontalView);
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g2","25"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g3","26"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g4","56"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g5","6"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g6","5"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g7","2"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g8","216"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g9","26"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"parle g10","256"));

        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvProductLayout.setLayoutManager(linearLayoutManager);
        rvProductLayout.setAdapter(horizontalProductScrollAdapter);
        horizontalProductScrollAdapter.notifyDataSetChanged();
        //---------------------------------------------

        //view Pager
        bannerSliderViewPager = view.findViewById(R.id.vpBannerSlieder);
        sliderModelList = new ArrayList<>();

        sliderModelList.add(new SliderModel(R.drawable.banner));
        sliderModelList.add(new SliderModel(R.drawable.logo));
        sliderModelList.add(new SliderModel(R.drawable.banner));
        sliderModelList.add(new SliderModel(R.drawable.logo));
        sliderModelList.add(new SliderModel(R.drawable.banner));
        sliderModelList.add(new SliderModel(R.drawable.logo));
        sliderModelList.add(new SliderModel(R.drawable.banner));
        sliderModelList.add(new SliderModel(R.drawable.logo));
        sliderModelList.add(new SliderModel(R.drawable.banner));
        sliderModelList.add(new SliderModel(R.drawable.logo));
        sliderModelList.add(new SliderModel(R.drawable.banner));
        sliderModelList.add(new SliderModel(R.drawable.logo));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {currentPage = position;}

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE){
                    pageLooper();
                }
            }
        };

        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
        startBannerSlideShow();
        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerSlideShow();
                if(event.getAction() == MotionEvent.ACTION_UP){
                    startBannerSlideShow();
                }
                return false;
            }
        });
        return view;
    }
    // show banner image in loop format
    private void pageLooper(){
        if(currentPage==sliderModelList.size()-2){
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }
        if(currentPage==1){
            currentPage = sliderModelList.size()-3;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }
    }

    private void startBannerSlideShow(){
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPage>=sliderModelList.size()){
                    currentPage =1 ;
                }
                bannerSliderViewPager.setCurrentItem(currentPage++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },DELAY_TIME,PERIOD_TIME);
    }

    private void stopBannerSlideShow(){
        timer.cancel();
    }
}