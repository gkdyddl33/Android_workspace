package com.koreait.permissonproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    int REQUEST_CODE=1; // 요청시 사용할 코드 규칙(개발자가 정한다)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void turnOn(View view){
        // 나 아닌 다른앱을 호출!!
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // startActivity(intent);      // 카메라 앱을 호출~
        // 현재앱에서 카메라 켜기

        // 유저에게 권한 수락요청
        // 과거버전 + 실제버전 -> 돌아갈 수 있는 .. Activity는 실제버전만..
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.CAMERA  // 배열..
        },REQUEST_CODE);
    }
    // onActivityResult랑 비슷함.. 즉 결과를 가져올때는 이러한 콜백 메서드가 지원..
    // 띠리서 개발자는 이 메서드를 통해 수락, 거절에 대한 처리를 할 수 있다..
    // grantResults 배열의 길이가 0이 아닐 때 사용자가 수락한 것으로 본다..
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 내가 요청시 보냈던 코드가 다시 왔다면..?
        // 즉, 내 요청에 대한 응답이라면
        if(requestCode==REQUEST_CODE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                // 위에 배열에 맞춰 조건을 정하자..승인 받고 있는..
                // 인간이라면? PackageManager.PERMISSION_GRANTED를 사용 = 카메라 권한을 승인 했다면..
                // 기계라면? 0 으로 표현..
                // 하고자 했던 작업을 그대로 진행하면 된다..
                AlertDialog.Builder builder =  new AlertDialog.Builder(this);    // 자료형..
                builder.setTitle("승인결과");
                builder.setMessage("승인해주셔서 감사합니다.");
                builder.setPositiveButton("예",null);
                builder.setNegativeButton("아니오",null);
                builder.create().show();

            }else{
                // 거절한 경우.. 이 앱을 더이상 사용할 수 없음을 의미한다.
                AlertDialog.Builder builder =  new AlertDialog.Builder(this);    // 자료형..
                builder.setTitle("승인결과");
                builder.setMessage("거절하시면 앱 사용을 못해요.");
                builder.setPositiveButton("예",null);
                builder.setNegativeButton("아니오",null);
                builder.create().show();
            }
        }
    }
}