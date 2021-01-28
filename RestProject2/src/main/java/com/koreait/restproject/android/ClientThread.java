package com.koreait.restproject.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread extends Thread{
   Socket socket;
   BufferedReader buffr;
   BufferedWriter buffw;
   private boolean flag=true;//쓰레드 가동 여부를 결정하는 논리값
   ChatClient chatClient;
   
   public ClientThread(Socket socket, ChatClient chatClient) {
      this.socket=socket;
      this.chatClient=chatClient;
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
         chatClient.area.append(msg+"\n");//로그에 남기기
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