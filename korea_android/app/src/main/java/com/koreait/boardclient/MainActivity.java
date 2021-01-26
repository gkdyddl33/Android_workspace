package com.koreait.boardclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText t_title;
    EditText t_writer;
    EditText t_content;
    ListView listView;
    String TAG = this.getClass().getName();

    BoardAdapter boardAdapter;
    HttpManager httpManager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리스트와 어댑터 연결
        //디자인이 복잡한(복합뷰) ListView는 BaseAdapter를 재정의해야 한다.
        listView=this.findViewById(R.id.listView);      // JTable
        t_title = this.findViewById(R.id.t_title);
        t_writer = this.findViewById(R.id.t_writer);
        t_content = this.findViewById(R.id.t_content);

        boardAdapter = new BoardAdapter(this);
        listView.setAdapter(boardAdapter);  // 리스트뷰와 어댑터와의 연결!!
        httpManager = new HttpManager();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                ArrayList<Board> boardList = bundle.getParcelableArrayList("boardList");
                // 어댑터에 리스트 주입 = 대체하기
                boardAdapter.list = boardList;
                boardAdapter.notifyDataSetChanged();
            }
        };
    }
    public void regist(View view){
        Thread thread = new Thread(){
            public void run() {
                // json생성하기
                try {
                    JSONObject json = new JSONObject();
                    json.put("title",t_title.getText().toString());  // 안드로이드 텍스트뷰 값..
                    json.put("writer",t_writer.getText().toString());  // 안드로이드 텍스트뷰 값..
                    json.put("content",t_content.getText().toString());  // 안드로이드 텍스트뷰 값..

                    Log.d(TAG,"json 스트링은"+json.toString());

                    httpManager.requestByPost("http://192.168.219.101:7777/rest/board",json.toString());// 여기서 잘 찍히면 HttpManager에서 응답서버코드 전송..즉, 데이터가 들어와잇다.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    public void getList(View view){
        // 네트워크 통신을 위한 쓰레드 생성 및 실행
        Thread thread = new Thread(){
            public void run() {
                ArrayList<Board> boardList = httpManager.requestByGet("http://192.168.219.101:7777/rest/board");

                // 핸들러에 요청 시점
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("boardList",boardList);
                message.setData(bundle);

                handler.sendMessage(message);  // UI에 대신 뭐좀 해달라고 핸들러에게 부탁한다.
            }
        };
        thread.start();
    }
}