package com.tenco.bank.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.handler.exception.UnAuthorizedException;

@ControllerAdvice // IoC 대상 (싱글톤 패턴) --> HTML 렌더링 예외에 많이 사용
public class GlobalControllerAdvice {
	
	/**
	 * 1. (개발시에 많이 활용)
	 * 모든 예외 클래스를 알 수 없기 때문에 로깅으로 확인할 수 있도록 설정
	 * 로깅 처리 - 동기적 방식(sysout) //  @slf4j (비동기 방식으로 로깅)
	 */
	@ExceptionHandler(Exception.class) // 
	public void exception(Exception e) {
		System.out.println("===========================");
		System.out.println(e.getClass().getName());
		System.out.println(e.getMessage());
		System.out.println("===========================");
	}
	
	/**
	 * Data로 예외를 내려 주는 방법
	 * @ResponseBody 활용
	 * - 브라우저에서 자바스크립트 코드로 동작
	 */
	
	// 예외를 내릴 때 데이터를 내리고 싶다면 1. @RestControllerAdvice 를 사용하면 된다.
	// 단 @ControllerAdvice 를 사용하고 있다면 @responseBody 를 붙여서 사용하면 된다.
	
	@ResponseBody
	@ExceptionHandler(DataDeleveryException.class)
	public String dataDeleveryException(DataDeleveryException e) {
		StringBuffer sb = new StringBuffer();
		sb.append(" <script>");
		sb.append(" alert('"+ e.getMessage() +"'); ");
		sb.append(" window.history.back(); ");
		sb.append(" </script> ");
		return sb.toString();
	}
	
	@ResponseBody
	@ExceptionHandler(UnAuthorizedException.class)
	public String unAuthorizedException(Exception e) {
		StringBuffer sb = new StringBuffer();
		sb.append(" <script>");
		sb.append(" alert('"+ e.getMessage() +"'); ");
		sb.append(" window.history.back(); ");
		sb.append(" </script> ");
		return sb.toString();
	}
	
	/**
	 * 에러 페이지로 이동 처리
	 * JSP로 이동시 데이터를 담아서 보내는 방법
	 * ModelAndView, Model 사용 가능
	 * throw new RedirectException("페이지가 존재하지 않음", 404);
	 */
	
	@ResponseBody
	@ExceptionHandler(RedirectException.class)
	public ModelAndView redirectException(RedirectException e) {
		
		ModelAndView modelAndView = new ModelAndView("errorPage"); 
		modelAndView.addObject("statusCode", e.getStatus().value());
		modelAndView.addObject("message", e.getMessage());
		return modelAndView; // 페이지 반환 + 데이터 내려 줌
	}
	
	
	
}
