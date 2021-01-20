package com.koreait.restproject.rest.controller.member;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koreait.restproject.model.domain.Member;

import lombok.extern.slf4j.Slf4j;

@RestController //Controller 에 ResponseBody가 탑재된 컨트롤러
@Slf4j
public class MemberController {
	
	@GetMapping("/member")
	public String getList() {
		log.debug("리스트 요청했어?");				
		return "ha ha ha";
	}
	
	@PostMapping("/member")
	public String regist(@RequestBody Member member) {
		System.out.println("등록을 원해?");
		System.out.println("m_id"+member.getM_id());		
		System.out.println("m_pass"+member.getM_pass());		
		System.out.println("m_name"+member.getM_name());		
		return "regist success";
	}
}