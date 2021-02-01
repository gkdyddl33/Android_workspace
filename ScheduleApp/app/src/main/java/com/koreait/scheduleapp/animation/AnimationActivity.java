package com.koreait.scheduleapp.animation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.koreait.scheduleapp.R;

public class AnimationActivity extends AppCompatActivity {
    ViewPager viewPager;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        viewPager = findViewById(R.id.viewPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager(),0);
        viewPager.setAdapter(adapter);  // 뷰페이지와 어댑터 연결..
    }
}