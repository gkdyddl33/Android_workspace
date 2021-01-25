package com.koreait.restproject.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koreait.restproject.exception.BoardUpdateException;
import com.koreait.restproject.message.Message;
import com.koreait.restproject.model.board.service.BoardService;
import com.koreait.restproject.model.domain.Board;

import lombok.extern.slf4j.Slf4j;

// restful url을 이해한다.. 또한 @ResponseBody가 이미 처리
@RestController
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;
	
   //목록 가져오기
   @GetMapping("/board")//이미 ResponseBody가 적용된 상태이므로, 컨버터만 등록해놓았다면, List는 자동으로
   //Json으로 변환되어 클라이언트의 응답 정보로 사용됨
   public List<Board> getList(){
      List boardList = boardService.selectAll();
      return boardList;
   }   
	   
   // 등록 요청
   @PostMapping("/board")
   //@RequestBody : 클라이언트가 전송한 json데이터를 자바의 객체로 변환 (json-->java로 변환)
   public ResponseEntity regist(@RequestBody Board board) {
      log.debug("title "+board.getTitle());
      log.debug("writer "+board.getWriter());
      log.debug("content "+board.getContent());
      
      boardService.regist(board);
         
      return ResponseEntity.ok().body(board);
   }

	//	한건 가져오기 : PK
   //{데이터가 들어온다..} = 변수로 보기 위해서는 @PathVariable  이 필요하다 = 경로의 일부분을 변수로 출력해라!!   
   @GetMapping("/board/{board_id}")
   public Board getDetail(@PathVariable int board_id) {
	   log.debug("클라이언트가 보낸 파라미터는 "+ board_id);
	   Board board = boardService.select(board_id);
	   return board;
   }
	// 수정요청
   @PutMapping("/board")
   public ResponseEntity<Board> update(@RequestBody Board board){// json으로 넘겨주고 있기 때문에 어노테이션 사용..
	   log.debug("board_id is "+board.getBoard_id());
      log.debug("title "+board.getTitle());
      log.debug("writer "+board.getWriter());
      log.debug("content "+board.getContent());
      boardService.update(board);
	   return ResponseEntity.ok().body(board);
   }
	// 삭제요청
   @DeleteMapping("/board/{board_id}")
   public ResponseEntity<Message> delete(@PathVariable int board_id) {
	   boardService.delete(board_id);
	   Message message = new Message();
	   message.setMsg("게시물 삭제 성공");
	   return ResponseEntity.ok().body(message);	// json으로 서버에 보내준다..
   }

}
