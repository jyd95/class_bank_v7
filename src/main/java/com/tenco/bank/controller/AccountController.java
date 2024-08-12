package com.tenco.bank.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tenco.bank.dto.DepositDTO;
import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.dto.TransferDTO;
import com.tenco.bank.dto.WithdrawalDTO;
import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.HistoryAccount;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;

@Controller // IoC 대상 (싱글톤으로 관리)
@RequestMapping("/account")
public class AccountController {
	
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
		
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
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
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new DataDeleveryException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeleveryException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		}
		if(dto.getBalance() == null || dto.getBalance() < 0) {
			throw new DataDeleveryException(Define.ENTER_YOUR_BALANCE, HttpStatus.BAD_REQUEST);
		}
		
		accountService.createAccount(dto, principal.getId());
		
		return "redirect:/index"; 
	}
	
	/**
	 * 계좌 목록 화면 요청
	 * 주소 설계 : http://localhost:8080/account/list,  ..../
	 * @return
	 */
	@GetMapping({"/list", "/"})
	public String listPage(Model model) {
		
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		System.out.println("111111111111");
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		List<Account> accountList = accountService.readAccountListByUserId(principal.getId());
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		}else {
			model.addAttribute("accountList", accountList);
		}
		return "account/list";
	}
	
	/**
	 * 출금 페이지 요청
	 * @return withdrawal.jsp;
	 */
	@GetMapping("/withdrawal")
	public String withdrawalPage() {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		return "account/withdrawal";
	}
	
	/**
	 * 
	 * @return
	 */
	@PostMapping("/withdrawal")
	public String withdrawalProc(WithdrawalDTO dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		
		// 유효성 검사 (자바 코드를 개발) --> 스프링 부트 @Valid 라이브러리 존재
		if(dto.getAmount() == null ) {
			throw new DataDeleveryException(Define.ENTER_YOUR_BALANCE, HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <= 0) {
			throw new DataDeleveryException(Define.W_BALANCE_VALUE, HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null) {
			throw new DataDeleveryException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountPassword() == null || dto.getWAccountPassword().isEmpty()) {
			throw new DataDeleveryException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountWithdraw(dto, principal.getId());
		return "redirect:/account/list";
	}
	
	/**
	 * 입금 페이지 요청
	 * @return deposit.jsp
	 */
	@GetMapping("/deposit")
	public String depositPage() {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		return "account/deposit";
	}
	
	
	
	/**
	 * 입금 처리 기능 만들기 
	 * @param dto
	 * @return account/list
	 */
	@PostMapping("/deposit")
	public String depositProc(DepositDTO dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		if(dto.getAmount().longValue() <= 0) {
			throw new DataDeleveryException(Define.D_BALANCE_VALUE, HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null|| dto.getDAccountNumber().trim().isEmpty()) {
			throw new DataDeleveryException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount() == null ) {
			throw new DataDeleveryException(Define.ENTER_YOUR_BALANCE, HttpStatus.BAD_REQUEST);
		}
		accountService.updateAccountDeposit(dto, principal.getId());
		return "redirect:/account/list";
	}
	
	/**
	 * 이체 페이지 요청
	 */
	@GetMapping("/transfer")
	public String transferPage() {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		return "account/transfer";
	}
	
	
	/**
	 * 이체 기능 요청
	 */
	@PostMapping("/transfer")
	public String transferProc(TransferDTO dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		}
		if(dto.getAmount() == null) {
			throw new DataDeleveryException(Define.ENTER_YOUR_BALANCE, HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <= 0) {
			throw new DataDeleveryException(Define.D_BALANCE_VALUE, HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null|| dto.getDAccountNumber().trim().isEmpty()) {
			throw new DataDeleveryException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null|| dto.getDAccountNumber().trim().isEmpty()) {
			throw new DataDeleveryException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeleveryException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		}
		accountService.updateAccountTransfer(dto, principal.getId());
		return "redirect:/account/list";
	}
	
	/**
	 * 계좌 상세 보기 페이지 
	 * 주소 설계 : http://localhost:8080/account/detail/1?type=all, deposit, withdraw
	 * @return
	 */
	@GetMapping("/detail/{accountId}")
	public String detail(@PathVariable(name = "accountId") Integer accountId, 
						@RequestParam(required = false, name ="type") String type,
						@RequestParam(name ="page", defaultValue = "1") int page,
						@RequestParam(name ="siez", defaultValue = "2") int size, 
						Model model) {
		
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException(Define.NOT_AN_AUTHENTICATED_USER, HttpStatus.UNAUTHORIZED);
		} 
		
		System.out.println("account : " + accountId);
		System.out.println("type : " + type);
		// 유효성 검사
		List<String> validTypes = Arrays.asList("all", "deposit", "withdrawal");
		
		if(!validTypes.contains(type)) {
			throw new DataDeleveryException("유효하지 않은 접근입니다.", HttpStatus.BAD_REQUEST);
		}
		
		// 페이지 갯수 계산
		int totalRecords = accountService.countHistoryByAccountIdAndType(type, accountId);
		int totalPages = (int)Math.ceil((double) totalRecords / size);
		
		Account account = accountService.readAccountById(accountId);
		List<HistoryAccount> historyList = accountService.readHistoryByAccountId(type, accountId, page, size);
		
		
		model.addAttribute("account", account);
		model.addAttribute("historyList", historyList);
		model.addAttribute("curruntPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("type", type);
		model.addAttribute("size", size);
		return "account/detail";
	}
}
