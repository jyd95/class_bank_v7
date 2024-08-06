package com.tenco.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.handler.exception.UnAuthorizedException;

@Controller // IoC 대상(싱글톤 패턴으로 관리) --> 제어의 역전
// 개발자가 new 하는것이 아니라 프레임워크가 메모리에 띄워줌
public class MainController {

	// REST API 기반으로 주소설계 가능

	// 주소설계
	// http:localhost:8080/main-page
	@GetMapping({ "main-page", "index" })
	// @ResponseBody
	public String mainPage() {
		System.out.println("mainPage() 호출 확인");
		// [JSP 파 일 찾기 (yml 설정)] - 뷰 리졸버
		// prefix : /WEB-INF/view
		// /main
		// suffix : .jsp
		return "main";
	}

	// TODO - 삭제 예정
	// 주소 설계
	// http://localhost:8080/error-test1/true
	// http://localhost:8080/error-test1/false

	@GetMapping("/error-test1/{isError}")
	public String errorPage(@PathVariable("isError") boolean isError) {
		System.out.println("1111111111");
		if (isError) {
			throw new RedirectException("잘못된 요청입니다.", HttpStatus.NOT_FOUND);
		}

		return "main";
	}

	// 주소 설계
	// http://localhost:8080/error-test2/true
	// http://localhost:8080/error-test2/false
	@GetMapping("/error-test2/{isError}")
	public String errorData(@PathVariable("isError") boolean isError) {

		if (isError) {
			throw new DataDeleveryException("잘못된 데이터 입니다.", HttpStatus.BAD_REQUEST);
		}

		return "main";
	}

	// 주소 설계
	// http://localhost:8080/error-test3/true
	// http://localhost:8080/error-test3/false
	@GetMapping("/error-test3/{isError}")
	public String errorData3(@PathVariable("isError") boolean isError) {

		if (isError) {
			throw new UnAuthorizedException("인가되지 않은 사용자 입니다..", HttpStatus.UNAUTHORIZED);
		}

		return "main";
	}
}
