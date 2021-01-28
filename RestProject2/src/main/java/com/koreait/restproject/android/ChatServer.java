package com.koreait.restproject.android;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatServer extends JFrame{
   JPanel p_north;
   JTextField t_port;
   JButton bt_start;
   JTextArea area;
   JScrollPane scroll;
   Thread thread;//서버 가동 쓰레드, 메인쓰레드가 서버소켓의 accept()에 의해 대기상테에 빠지면 안 되므로 
   ServerSocket server;
   ChatServer chatServer;
   Vector<ChatThread> vec;//접속한 클라이언트 마다 생성된 ChatThread를 보관해놓을 컬렉션
   
   public ChatServer() {
      p_north=new JPanel();
      t_port=new JTextField("9999", 20);
      bt_start=new JButton("가동");
      area=new JTextArea();
      scroll=new JScrollPane(area);
      chatServer=this;
      vec=new Vector<ChatThread>();
      
      p_north.add(t_port);
      p_north.add(bt_start);
      add(p_north, BorderLayout.NORTH);
      add(scroll);
      
      bt_start.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            startServer();
         }
      });
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            stopServer();
         }
      });
      setSize(300, 400);
      setVisible(true);
   }
   
   public void startServer() {
      thread=new Thread() {
         public void run() {
            try {
               server=new ServerSocket(Integer.parseInt(t_port.getText()));
               area.append("서버 가동\n");
               while(true) {
                  Socket socket=server.accept();
                  area.append("접속 감지\n");
                  
                  ChatThread chatThread=new ChatThread(socket, chatServer);
                  chatThread.start();//대화 시작
                  
                  //벡터에 지금 생성된 추가(곧 접속자 명단을 얻을 수 있다.)
                  vec.add(chatThread);
                  area.append("현재 접속자 수 : "+vec.size()+"\n");
               }
               
            } catch (NumberFormatException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      };
      thread.start();
   }
   
   public void stopServer() {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
   }
   
   public static void main(String[] args) {
      new ChatServer();
   }
}