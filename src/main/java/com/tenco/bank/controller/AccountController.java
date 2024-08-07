package com.tenco.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.AccountService;

import jakarta.servlet.http.HttpSession;

@Controller // IoC 대상 (싱글톤으로 관리)
@RequestMapping("/account")
public class AccountController {
	
	// 계좌 생성 화면 요청
	@Autowired
	private final HttpSession session;
	@Autowired
	private final AccountService accountService;
	
	@Autowired
	public AccountController(AccountService accountService,HttpSession session) {
		this.session = session;
		this.accountService = accountService;
	}
	
	/**
	 * 계좌 생성 페이지 요청
	 * 주소 설계 : http://localhost:8080/account/save
	 * @return save.jsp
	 */
	@GetMapping("/save")
	public String savePage() {
		
		// 1. 인증검사 필요 (account 전체)
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
			
		return "account/save";
	}
	
	/**
	 * 계좌 생성 요청 처리
	 * @param dto
	 * @return
	 */
	@PostMapping("/save")
	public String saveProc(SaveDTO dto) {
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new DataDeleveryException("계좌번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeleveryException("비밀번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getBalance() == null || dto.getBalance() < 0) {
			throw new DataDeleveryException("잔액 입력이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
		}
		
		accountService.createAccount(dto, principal.getId());
		
		return "redirect:/index"; 
	}
}
