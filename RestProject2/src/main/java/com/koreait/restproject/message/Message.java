package com.koreait.restproject.message;

import lombok.Data;

// 이 객체에 대하 getter/setter 를 정의해주세요.
@Data
public class Message {
	// 클라이언트가 받게 될 에러 메시지
	private String msg;	
	// http 상태코드 200, 404, 500, 403.. 
	private int statusCode;
	
}
