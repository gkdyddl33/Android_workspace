package com.koreait.actionbarapp.chat;

import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//서버로부터 전송되어온 메시지를 무한루프로 청취해야 하므로,
//메인쓰레드로 하면 에러가 나니, 별도의 쓰레드가 필요하다
public class ChatThread extends Thread{
    Socket socket;
    BufferedReader buffr;
    BufferedWriter buffw;
    private boolean flag=true;//쓰레드 가동 여부를 결정하는 논리값
    ChatFragment chatFragment;

    public ChatThread(Socket socket, ChatFragment chatFragment) {
        this.socket=socket;
        this.chatFragment=chatFragment;

        try {
            buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //메시지 청취
    public void listen() {
        String msg=null;
        try {
            msg=buffr.readLine();
            //textView 로그에 남기기. 디자인에 대한 접근은 개발자 정의 쓰레드에서 진행할 수 없다.
            //금지사항
            //chatFragment.t_log.append(msg+"\n");
            Message message=new Message();
            Bundle bundle=new Bundle();
            bundle.putString("msg", msg);
            message.setData(bundle);
            chatFragment.handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //메시지 전송
    public void send(String msg) {
        try {
            buffw.write(msg+"\n");
            buffw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(flag) {
            listen();
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}