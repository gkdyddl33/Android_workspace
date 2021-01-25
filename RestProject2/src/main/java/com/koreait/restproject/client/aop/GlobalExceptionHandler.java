package com.koreait.restproject.client.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.koreait.restproject.exception.BoardUpdateException;
import com.koreait.restproject.exception.MemberListException;
import com.koreait.restproject.message.Message;

import lombok.extern.slf4j.Slf4j;

// 일반요청에 대한 글로벌 예외처리
// 모든 컨트롤러..
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	// 회원목록을 가져올 때 발생하는 예외처리	
	@ExceptionHandler(MemberListException.class)
	public String handle(MemberListException e) {
		log.debug("일반 요청 시 발생하는 예외처리");
		return null;
	}

}
