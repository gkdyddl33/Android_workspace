package com.koreait.pageapp.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.koreait.pageapp.R;

public class FormActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG=this.getClass().getName();
    EditText t_id;
    EditText t_pass;
    EditText t_name;
    private static final int REQUEST_CODE =1;   // 요청구분코드..
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        //위젯 버튼과 리스너와의 연결
        Button bt_send=(Button)findViewById(R.id.bt_send);
        Button bt_dial=(Button)findViewById(R.id.bt_dial);
        t_id=(EditText)findViewById(R.id.t_id);
        t_pass=(EditText)findViewById(R.id.t_pass);
        t_name=(EditText)findViewById(R.id.t_name);

        bt_send.setOnClickListener(this);//위젯 버튼과 리스너와의 연결
        bt_dial.setOnClickListener(this);//위젯 버튼과 리스너와의 연결
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"?");
        if(v.getId()==R.id.bt_send){
            send();
        }else if(v.getId()==R.id.bt_receive){
            sendAndGet();
        }else if(v.getId()==R.id.bt_dial){
            callPhone();
        }
    }

    //다른 액티비티를 호출
    public void send(){
        //아래와 같이 대상 클래스를 정확히 명시하는 인텐트 사용법을 가리켜 명시적(explicit) 인텐트라 한다.
        Intent intent=new Intent(this, ReceiveActivity.class);

        //intent는 jsp의 request, session, application 객체처럼 데이터를 심을 수 있다.
        Member member=new Member();
        member.setId(t_id.getText().toString());
        member.setId(t_pass.getText().toString());
        member.setId(t_name.getText().toString());

        //인텐트에 데이터 심기
        Bundle bundle=new Bundle();
        bundle.putParcelable("member", member);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    public void sendAndGet(){
        Intent intent = new Intent(this,ResultActivity.class);
        Member member = new Member();

        member.setId(t_id.getText().toString());
        member.setPass(t_pass.getText().toString());
        member.setName(t_name.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putParcelable("member",member);
        intent.putExtra("data",bundle);

        // 그냥 출발이 아니라, 결과를 받아올 것을 염두해 둔 출발
        // 매개변수 : 전달할 데이터, 요청 구분코드
        setResult(this,REQUEST_CODE);
    }

    public void callPhone(){
        //우리가 제죽중인 현재 앱안에 있는 액티비티가 아니라, 외부 애브이 액티비티명??
        //결론 : 다른 앱의 액티비티는 우리가 알 수도 없고 알 필요도 없다..이때는 명시적이 아닌
        //암시적, 묵시적(implicit) 인텐트를 사용해야 한다. 한마디로 얼버무림
        //안드로이드의 intent가 자체적으로 가진 상수들이 있다.. 이 상수들을 이용하면 안드로이드가
        //해석하는데 도움이 된다.
        Intent intent=new Intent(Intent.ACTION_DIAL);
        startActivity(intent);
    }
}