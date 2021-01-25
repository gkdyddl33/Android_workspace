package com.koreait.restproject.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koreait.restproject.message.Message;
import com.koreait.restproject.model.domain.Member;
import com.koreait.restproject.model.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;


// rest요청을 처리하는 컨트롤러
@RestController //Controller 에 ResponseBody가 탑재된 컨트롤러
@Slf4j
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	// jsp 페이지를 반환하지 말고 데이터 전송
	@GetMapping("/member")
	public ResponseEntity<List<Member>> getList() {
		log.debug("rest 리스트 요청했어?");		
		List memberList = (List)memberService.selectAll();
		
		ResponseEntity entity = ResponseEntity.ok().body(memberList);	// ok(성공메서드)
		return entity;
	}
	
		
	@PostMapping("/member")
	public String regist(@RequestBody Member member) {//client에서 json으로 날릴 떄, @RequestBody가 매핑을 시켜준다 해당 member vo와.. jackson필요..!!
		log.debug("등록을 원해?");
		log.debug("m_id"+member.getM_id());
		log.debug("m_pass"+member.getM_pass());
		log.debug("m_name"+member.getM_name());
		
		memberService.regist(member);
		
		return "regist success"; //REST에서는 개발자가 클라이언트에게 도대체 뭘 반환해야 할까?
	}
}