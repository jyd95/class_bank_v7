package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.dto.WithdrawalDTO;
import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.History;
import com.tenco.bank.utils.Define;

@Service
public class AccountService {
	
	@Autowired
	private final AccountRepository accountRepository;
	private final HistoryRepository historyRepository;
	
	@Autowired
	public AccountService(AccountRepository accountRepository, HistoryRepository historyRepository) {
		this.accountRepository = accountRepository;
		this.historyRepository = historyRepository;
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
			throw new DataDeleveryException(Define.EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RedirectException(Define.UNKNOWN, HttpStatus.SERVICE_UNAVAILABLE);
		}
		if(result != 1) {
			throw new DataDeleveryException(Define.FAIL_TO_CREATE_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public List<Account> readAccountListByUserId(Integer userId) {
		List<Account> accountListEntity = null; 
		try {
			accountListEntity = accountRepository.findByUserId(userId);
		} catch (DataAccessException e) {
			throw new DataDeleveryException(Define.INVALID_INPUT, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			throw new RedirectException(Define.UNKNOWN, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return accountListEntity;
	}
	
	// 1. 계좌 존재 여부 확인 -> select / R(READ)
	// 2. 본인 계좌 여부 확인 -> 객체 상태값 비교
	// 3. 계좌 비밀번호 확인 ->  객체 상태값 비교
	// 4. 잔액 여부 확인 -> 객체 상태값 비교
	// 5. 출금 처리 -> update / U(UPDATE)
	// 6. 히스토리_tb 등록 -> insert / C(CREATE)
	// 7. 트랜젝션 처리
	@Transactional
	public void updateAccountWithdraw(WithdrawalDTO dto, Integer principalId) {
		// 1.
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if(accountEntity == null) {
			throw new DataDeleveryException(Define.NOT_EXIST_ACCOUNT, HttpStatus.BAD_REQUEST);
		}
		// 2. 
		accountEntity.checkOwner(principalId);
		// 3.
		accountEntity.checkPassword(dto.getWAccountPassword());
		// 4.
		accountEntity.checkBalance(dto.getAmount());
		// 5.
		// accountEntity 객체의 잔액을 변경 하고 업데이트 처리
		accountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(accountEntity);
		
		// 6. 
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(accountEntity.getBalance());
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null);
		int rowResultCount = historyRepository.insert(history);
		if(rowResultCount != 1) {
			throw new DataDeleveryException(Define.FAILED_PROCESSING, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
