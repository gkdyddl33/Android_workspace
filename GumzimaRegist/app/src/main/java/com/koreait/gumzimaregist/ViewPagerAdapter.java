package com.koreait.gumzimaregist;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.koreait.gumzimaregist.login.HomeFragment;
import com.koreait.gumzimaregist.login.LoginFragement;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    Fragment[] fragments=new Fragment[2];
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragments[0]=new HomeFragment();
        fragments[1]=new LoginFragement();

    }

    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}