package com.koreait.boardapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/*
 * 안드로이드는 메인쓰레드가 루프나, 대기상태, 웹요청등을 수행하면 그 자체로
 * 에러 사항이다!! 왜 메인쓰레드는 UI, Event 등을 처리해야 하는 앱운영쓰레드 이므로...
 * 만일 위와같은 금지된 작업을 수행하면 앱은 무한 대기 상태에 빠지게 되므로..
 * */

public class HttpManager extends Thread{
    String TAG=this.getClass().getName();
    URL url;
    HttpURLConnection con; //http통신을 위한 객체(헤더+바디를 구성하여 서버와 데이터를 주고받는
                                        // stateless 한 통신)
    // 쓰레드의 요청을 대기열(ThreadQueue) 에 적재시켰다가 순서대로 mainThread에게 일 시키도록 중재한다.
    Handler handler;
    MainActivity mainActivity;

    //이 객체를 생성하는 者는 주소와 제이슨 데이터를넘겨야 한다
    public HttpManager(MainActivity mainActivity){
        this.mainActivity=mainActivity;
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d(TAG,"메인에 반영해줄까?");
                // 쓰레드 대신, ListView를 제어하자.
                Bundle bundle = msg.getData();

                List boardList = (List)bundle.getParcelableArrayList("boardList");
                Log.d(TAG,"번들에서 끄집어낸 객체의 제목은 "+boardList.size());
                mainActivity.boardAdapter.data = boardList; // 데이터교체
                mainActivity.boardAdapter.notifyDataSetChanged();   // 데이터 갱신신
            }
       };
    }

    public void requestByGet(String requestUrl){ //Get방식으로 요청을 시도하는 메서드
        BufferedReader buffr=null;

        try{
            url = new URL(requestUrl);//요청 주소
            con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            //서버로부터 응답 데이터 가져오기
            buffr=new BufferedReader(new InputStreamReader(con.getInputStream())); //바이트 기반 기반 스트림

            StringBuilder sb = new StringBuilder(); //문자열을 누적할 객체
            String data=null;
            while(true){
                data=buffr.readLine(); //한줄 읽어들인다...
                if(data==null)break; //읽어들일 데이터가 없다면 무한루프 종료
                sb.append(data);//읽어들인 문자열을 누적시키자
            }
            Log.d(TAG, "서버가 보낸 응답 데이터는 : "+sb.toString());

            int code=con.getResponseCode(); //서버로부터 받은 응답코드 반환 ( 이 시점에 이미 서버에 요청을 완료 후 응답도 받은 상태)
            Log.d(TAG,"서버로부터 받은 응답 코드는 "+code);

            // 리스트뷰 등의 View 에 반영해보자. 만일 메인쓰레드가 담당하는 디자인 제어기능을, 개발자가 정의한 쓰레드가 동시에 같이 만진다고 상상해보자.
            // 그래픽 처리의 안정적인 동기화를 위해 금기사항..
            // 데이터를 교환하기 위해서는 json을 사용한 것이기 때문에 안드로이드나 스프링에서는 다시 POJO로 변환하여
            // 사용하면 유지보수가 쉽다. 코드의 중립성 유지..
            ArrayList<Board> boardList = new ArrayList();
            try {
                // 문자열을 JSON 객체화
                JSONArray jsonArray=new JSONArray(sb.toString());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=(JSONObject) jsonArray.get(i);
                    Board board=new Board();
                    board.setBoard_id(jsonObject.getInt("board_id"));
                    board.setTitle(jsonObject.getString("title"));
                    board.setWriter(jsonObject.getString("writer"));
                    board.setContent(jsonObject.getString("content"));
                    board.setRegdate(jsonObject.getString("regdate"));
                    board.setHit(jsonObject.getInt("hit"));
                    boardList.add(board);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 핸들러에게 전달할 데이터 구성하기!
            Message message = new Message();
            Bundle bundle = new Bundle();
            Board board = new Board();
            board.setTitle("직렬화 테스트");

            //
            bundle.putParcelableArrayList("boardList",boardList);

            message.setData(bundle);
            handler.handleMessage(message);

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(buffr!=null){
                try{
                    buffr.close();
                }catch(IOException e){
                }
            }
        }
    }

    //Post방식의 요청을 시도하되, JSON데이터를 전송하겠다!!!
    public int requestByPost(String requestUrl,String data){
        BufferedWriter buffw=null; //버퍼처리된 문자기반 스트림
        int code=0; //서버의 응답 코드

        try{
            url = new URL(requestUrl);//요청 주소
            con=(HttpURLConnection)url.openConnection();
            //데이터형식을 헤더에 첨가해줘야, 서버측에서 제이슨데이터가 전송되어 온것임을 안다..이게 바로 HTTP프로토콜간의 약속이다
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");
            con.setRequestMethod("POST");
            con.setDoOutput(true);//서버에 데이터를 출력하기 위해 필요한 옵션!!
            //요청을 떠나기 전에, 준비할게 잇다면 여기서 준비하자!!, json 문자열을 준비하자!!
            //JSON 객체 자체가 아닌 문자열로 준비하는 이유는?
			/*
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"m_id\":\"batman\",");
			sb.append("\"m_pass\":\"1234\",");
			sb.append("\"m_name\":\"배트맨\"");
			sb.append("}");
			*/

            //실행중인 프로그램에서 서버로 데이터를 보내야 하므로, 출력스트림으로 처리하자!!
            buffw=new BufferedWriter(new OutputStreamWriter(con.getOutputStream(),"UTF-8")); //한글 인코딩 처리해야 함
            buffw.write(data);
            buffw.flush();

            code=con.getResponseCode(); //요청 + 응답이 발생
            System.out.println("서버로 부터 받은 응답 코드는 "+code);

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(buffw!=null){
                try{
                    buffw.close();
                }catch(IOException e){
                }
            }
        }
        return code; //응답코드 반환
    }
}