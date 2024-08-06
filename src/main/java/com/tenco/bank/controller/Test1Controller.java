package com.tenco.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.User;

// 기존 @Controller -> 리턴 String 시 (뷰 리졸버 동작 --> JSP 파일 찾아 렌더링 처리 함.)
// RestController --> 뷰 리졸버 x  JSP 안타고 그냥 데이터를 반환 처리
@RestController 	// @Controller + @ResponseBody
public class Test1Controller {
	
	// http://localhost:8080/test1
	@GetMapping("/test1")
	public User test1() {
		// 원래는 String 값 Gson 라이브러리 사용 --> JSON 형식으로 변환 후 응답 처리
		// springboot 는 생략가능
		try {
			int result = 10 / 0;
		} catch (Exception e) {
			throw new UnAuthorizedException("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
		}
		return User.builder().username("길동").password("asd123").build();
	}
	
}
