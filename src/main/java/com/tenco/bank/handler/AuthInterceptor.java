package com.tenco.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component  // IoC 대상 , 싱글톤  패턴
public class AuthInterceptor implements HandlerInterceptor{
	
	// preHandle 의 동작 흐름 (스프링부트 설정 파일, 설정 class에 등록 되어야 함 : 특정URL)
	// 컨트롤러에 들어 오기 전에 동작한다.
	// return 값 boolean
	// true 일 때 => 컨트롤러 안으로 들여 보냄
	// false 일 때 => 컨트롤러 안으로 못 들어감
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 하지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
		}
		
		return true;
	}
	
	// postHandle 의 동작흐름
	// 뷰가 렌더링 되기 직전에 콜백되는 메서드
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	// afterCompletion
	// 요청 처리가 완료된 후, 즉 뷰가 완전히 렌더링 된 후에 호출된다.
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
