package com.koreait.pageapp.fragement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.koreait.pageapp.R;

public class PagerActivity extends AppCompatActivity {
    ViewPager viewPager;
    MyViewPagerAdapter adapter;

    // init 생성자..
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        // 뷰 페이저와 어댑터와의 연결!!
        adapter = new MyViewPagerAdapter(this.getSupportFragmentManager(),0);
        viewPager.setAdapter(adapter);
    }

    public void showPage(View view){
        switch (view.getId()){
            case R.id.bt_red:flowPage(0);break;
            case R.id.bt_blue:flowPage(1);break;
            case R.id.bt_yellow:flowPage(2);break;
        }
    }

    // 페이지 자동 스트롤
    public void flowPage(int position){
        viewPager.setCurrentItem(position,true);    // 부드럽게 해당페이지로 옮겨 준다..
    }
}