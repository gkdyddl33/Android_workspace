package com.koreait.restproject.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koreait.restproject.model.board.service.BoardService;
import com.koreait.restproject.model.domain.Board;

import lombok.extern.slf4j.Slf4j;

// restful url을 이해한다.. 또한 @ResponseBody가 이미 처리
@RestController
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	// 목록가져오기
	// 이미 ResponseBody가 적용된 상태이므로, 컨버터만 등록해놓았다면  List 는 자동으로  JSON 으로 변환되어 클라이언트의 응답 정보로 사용됨..
	@GetMapping("/board")
	public List<Board> getList(){
		List boardList = boardService.selectAll();
		return boardList;
	}
	
	// 한건 가져오기
	// @RequestBody : 클라이언트가 전송한 json 데이터를 자바의 객체로 반환(json->java로 반환)
	@PostMapping("/board")
	public String regist(@RequestBody Board board) {
		log.debug("title is "+board.getTitle());
		log.debug("writer is "+board.getWriter());
		log.debug("content is "+board.getContent());
		
		boardService.regist(board);
		return null;
	}
	// 등록요청
	// 수정요청
	// 삭제요청
}
