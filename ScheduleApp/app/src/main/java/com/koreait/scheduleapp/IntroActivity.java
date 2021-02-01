package com.koreait.scheduleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
    }

    // 매인 액티비티 호출
    public void goMain(View view){
        Intent intent = new Intent(this,MainActivity.class);   // 의도를 표현.. 어디패키지에서? 어디로 갈래?
        startActivity(intent);  // 지정한 액티비티 호출!!
    }
}
