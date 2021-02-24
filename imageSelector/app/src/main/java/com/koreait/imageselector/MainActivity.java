package com.koreait.imageselector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();

    ImageView imageView;
    private static final int REQUEST_CODE=100;  // 다른요청들과 구분되는 나만의 요청 코드 정의..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }

    public void openFile(View view){
        // 갤러리 앱은, 나 아닌 다른 앱니다.. 따라서 갤러리 앱의 내부 데이터는 절대 접근이 불가능하고,
        // 오직 공개된 데이터만 접근할 수 있다..
        // 갤러리 앱 입장에서는, 외부의 앱이 접근할 수 있도록 콘텐트 프로바이를 통해
        // 인터페이스(=사용법, 조작법)를 제공한다..
        Intent intent = new Intent();   // 명시적? 암시적?
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        //startActivity(intent);    -- 휴대폰의 갤러리로 이동된다..
        // 결과를 가져올 수 있는 출발!!
        startActivityForResult(intent,REQUEST_CODE);
    }
    // 갤러리 앱에서 선택한 이미지를 추출하기!!

    // 이 메서드는 상대 액티비티가 작업을 완료하고, 닫힐 때 자동으로 호출됨..
    // 즉 우리의 경우, 이미지가 선택 후 돌아올 때 이 메서드 호출됨..
    // 매개변수 중, 세번째 인수인 Intent data는, 상대방 앱의 액티비티에서 사용된 Intent이다!!
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // 내가 보냈던 요청이라면..100이다..
        if(requestCode==REQUEST_CODE){

            // 상대방에서 처리한 결과가 성공이라면..
            // 안드로이드 자체에서 지원하는 상수로서, 성공의 의미를 정해놨다.
            if(requestCode==RESULT_OK){
                InputStream is =  null;
                try {
                    // 스트링을 얻어서 이미지로 출력!!
                    Uri uri =  intent.getData();

                    is = this.getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imageView.setImageBitmap(bitmap);   // 나 아닌 다른 앱이 지원하는 컨텐츠를 사용하게 하는 것..오픈 API 사용하듯이..
                    Log.d(TAG,"bitmap is ========= "+bitmap);
                    is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(is!=null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                Toast.makeText(this,"사진 선택취소",Toast.LENGTH_SHORT).show();
            }
        }
    }
}