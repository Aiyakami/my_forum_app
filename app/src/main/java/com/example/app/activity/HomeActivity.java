package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.app.fragment.CollectFragment;
import com.example.app.fragment.HomeFragment;
import com.example.app.fragment.MyFragment;
import com.example.app.adapter.MyPagerAdapter;
import com.example.app.NonSwipeableViewPager;
import com.example.app.R;
import com.example.app.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private String[] mTitles = {"首页", "收藏", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.collect_unselect,
            R.mipmap.tab_contact_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.collect_selected,
            R.mipmap.tab_contact_select};

    private NonSwipeableViewPager viewpager;
    private CommonTabLayout commontablaout;


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewpager = findViewById(R.id.Viewpager);
        viewpager.setPagingEnabled(false);
        commontablaout=findViewById(R.id.CommonTabLayout);
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(CollectFragment.newInstance());
        mFragments.add(MyFragment.newInstance());

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        commontablaout.setTabData(mTabEntities);
        commontablaout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                viewpager.setCurrentItem(position);
                //当position==2式，应该是个人界面，此时发送get请求，并且改写myfragment，同时将数据存到本地

                //首先需要判断是否有缓存

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mTitles,mFragments));//MyPagerAdapter需要重写且需要修改被弃用的放啊


    }
}