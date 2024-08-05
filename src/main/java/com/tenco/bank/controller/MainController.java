package com.tenco.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // IoC 대상(싱글톤 패턴으로 관리) --> 제어의 역전 
// 개발자가 new 하는것이 아니라 프레임워크가 메모리에 띄워줌
public class MainController {
	
	// REST API 기반으로 주소설계 가능
	
	// 주소설계
	// http:localhost:8080/main-page
	
	@GetMapping("/main-page")
	public String mainPage() {
		System.out.println("mainPage() 호출 확인");
		//[JSP 파 일 찾기 (yml 설정)] - 뷰 리졸버
		// prefix : /WEB-INF/view
		// 			/main
		// suffix : .jsp
		return "/main";
	}
	
}
