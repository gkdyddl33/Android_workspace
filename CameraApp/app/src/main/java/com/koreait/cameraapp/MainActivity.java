package com.koreait.cameraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST =1;  // 개발자가 만든 요청 코드
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = findViewById(R.id.imageView);
        setContentView(R.layout.activity_main);
    }

    public void openCamera(View view){
        // 다른 앱을 호출할려면, 암시적 인텐트 이용해야 한다. 왜? 그 앱을 제작한 개발자가 만든 액티비티명을 모르기 떄문에..
        // 또한, 다른 앱을 접근할 수 없기 때문에 안드로이드 시스템에게 간접적으로 열어줄 것 을 부탁해야 하기 떄문에 직접 호출이 불가..

        // 2015년 마시멜로 버전 이후부터는 보안이 강화 되었기 때문에 AndroidMainfest.xml에 퍼미션을 명시하더라도, 보안 취득을 인정하지 않는다..
        // 위험한 (Dangerous) 권한은 사용자에게 요청하게 바뀜..

        // 권한을 취득한 경우만 카메라 앱을 호출해야 한다..
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.CAMERA
        },CAMERA_REQUEST);   // 내가 만든 권한..

    }

    // 권한 요청에 대한 사용자의 처리 결과 가져오기!
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CAMERA_REQUEST){
            // 카메라 요청에 대한 응답인지 여부..
            // 승인 OR 거절
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                // 허락을 줄게 .. 카메라 앱을 호출하기 위해 intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);  // 이미지를 찍고, 이미지를 가져오게 하기 위해..
            }else{
                // 못한다면 알려주자.
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("승인 결과");
                alert.setMessage("서비스를 이용하시려면 '앱 사용중에만 허용'으로 전환하셔야 합니다.");
                alert.create().show();

                // 해당 앱의 권한 설정 화면으로 전환..
                gotoConfig();
            }
        }
    }

    public void gotoConfig(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        // 설정화면 중에서도, 현재 앱을 찾아가도록 추가 정보를 더 인테트에 넣어주자.
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);    // 기존 인텐트에 uri 추가!
        startActivity(intent);
    }

    // 요청 후 결과를 가져올게 있다면? 해당 메서드 재정의..카메라 앱 or 갤러리 앱
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 이 메서드가 어떤 앱으로부터 온 응답인지 구분을 해야 한다..
        if(requestCode==CAMERA_REQUEST){
            // 카메라 앱으로부터 온 응답이라면..
            System.out.println("카메라 사진 촬영 후 응답!");
            Bundle bundle = data.getExtras();   // 촬영된 이미지..
            Bitmap bitmap = (Bitmap) bundle.get("data"); // 이미지 정해진 키 값인 data를 대입한다.
            imageView.setImageBitmap(bitmap);
        }
    }
}