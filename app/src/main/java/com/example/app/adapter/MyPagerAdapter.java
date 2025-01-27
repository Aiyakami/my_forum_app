package com.example.app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private ArrayList<Fragment> mFragments;

    public MyPagerAdapter(FragmentManager fm,String[] mTitles,ArrayList<Fragment> mFragments) {
        super(fm);
        this.mFragments=mFragments;
        this.mTitles=mTitles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}