package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.model.Account;

import jakarta.servlet.http.HttpSession;

@Service
public class AccountService {
	
	@Autowired
	private final HttpSession session;
	private final AccountRepository accountRepository;
	
	@Autowired
	public AccountService(AccountRepository accountRepository, HttpSession session) {
		this.accountRepository = accountRepository;
		this.session = session;
	}
	
	/**
	 * 계좌 생성 기능
	 * @param dto
	 * @param principalId
	 */
	// 트랜잭션 처리
	@Transactional
	public void createAccount(SaveDTO dto, Integer principalId) {
		int result = 0;
		try {
			result = accountRepository.insert(dto.toAccount(principalId)); 
		} catch (DataAccessException e) {
			throw new DataDeleveryException("중복된 계좌번호입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RedirectException("알 수 없는 오류.", HttpStatus.SERVICE_UNAVAILABLE);
		}
		if(result != 1) {
			throw new DataDeleveryException("계좌 개설에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public List<Account> readAccountListByUserId(Integer userId) {
		List<Account> accountListEntity = null; 
		try {
			accountListEntity = accountRepository.findByUserId(userId);
		} catch (DataAccessException e) {
			throw new DataDeleveryException("잘못된 처리 입니다", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			throw new RedirectException("알 수 없는 오류", HttpStatus.SERVICE_UNAVAILABLE);
		}
		return accountListEntity;
	}
}
