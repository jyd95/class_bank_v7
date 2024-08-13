package com.tenco.bank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.DepositDTO;
import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.dto.TransferDTO;
import com.tenco.bank.dto.WithdrawalDTO;
import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.History;
import com.tenco.bank.repository.model.HistoryAccount;
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
	
	// 1. 계좌 존재 여부 확인 -> select / R(READ)
	// 2. 본인 계좌 여부 확인 -> 객체 상태값 비교
	// 3. 입금 처리 -> update / U(UPDATE)
	// 4. 히스토리_tb 등록 -> insert / C(CREATE)
	@Transactional
	public void updateAccountDeposit(DepositDTO dto, Integer principalId) {
		// 1.
		Account accountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		if(accountEntity == null) {
			throw new DataDeleveryException(Define.NOT_EXIST_ACCOUNT, HttpStatus.BAD_REQUEST);
		}
		// 2.
		accountEntity.checkOwner(principalId);
		// 3.
		accountEntity.deposit(dto.getAmount());
		accountRepository.updateById(accountEntity);
		// 4.
		History history = History.builder()
									.amount(dto.getAmount())
									.dAccountId(accountEntity.getId())
									.dBalance(accountEntity.getBalance())
									.wAccountId(null)
									.wBalance(null)
									.build();
		int rowResultCount = historyRepository.insert(history);
		if(rowResultCount != 1) {
			throw new DataDeleveryException(Define.FAILED_PROCESSING, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 이체 기능 만들기
	// 1. 출금 계좌 존재 여부 확인 -- select
	// 2. 입금 계좌 존재 여부 확인 -- select
	// 3. 출금 계좌 본인 소유 확인 -- 객체 상태값과 세션 아이디 비교
	// 4. 출금 계좌 비밀번호 확인 -- 객체 상태값과 dto 비밀번호 비교 
	// 5. 출금 계좌 잔액 여부 확인 -- 객체 상태값 확인, dto와 비교
	// 6. 입금 계좌 객체 상태값 변경 처리 (잔액 + 거래금액 처리)
	// 7. 입금 계좌 -- update 처리
	// 8. 입금 계좌 객체 상태값 변경 처리 (잔액 - 거래금액 처리)
	// 9. 출금 계좌 -- update 처리
	// 10. history -- insert 처리
	// 11. transaction 처리
	
	@Transactional
	public void updateAccountTransfer(TransferDTO dto, Integer principalId) {
		// 1.
		Account wAccountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if(wAccountEntity == null) {
			throw new DataDeleveryException(Define.NOT_EXIST_ACCOUNT, HttpStatus.BAD_REQUEST);
		}
		// 2.
		Account dAccountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		if(dAccountEntity == null) {
			throw new DataDeleveryException(Define.NOT_EXIST_ACCOUNT, HttpStatus.BAD_REQUEST);
		}
		// 3.
		wAccountEntity.checkOwner(principalId);
		// 4.
		wAccountEntity.checkPassword(dto.getPassword());
		// 5.
		wAccountEntity.checkBalance(dto.getAmount());
		// 6.
		wAccountEntity.withdraw(dto.getAmount());
		// 7.
		accountRepository.updateById(wAccountEntity);
		// 8.
		dAccountEntity.deposit(dto.getAmount());
		// 9.
		accountRepository.updateById(dAccountEntity);
		// 10.
		History history = History.builder()
				.amount(dto.getAmount())
				.dAccountId(dAccountEntity.getId())
				.dBalance(dAccountEntity.getBalance())
				.wAccountId(wAccountEntity.getId())
				.wBalance(wAccountEntity.getBalance())
				.build();
		int rowResultCount = historyRepository.insert(history);
		if(rowResultCount != 1) {
			throw new DataDeleveryException(Define.FAILED_PROCESSING, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 *  단일 계좌 조회 기능 
	 * @param accountId (pk)
	 * @return
	 */
	@Transactional
	public Account readAccountById(Integer accountId) {
		Account accountEntity = accountRepository.findByAccountId(accountId);
		if(accountEntity == null) {
			throw new DataDeleveryException(Define.NOT_EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return accountEntity;
	}
	/**
	 * 단일 계좌 거래 내역 조회 
	 * @param type = [all, deposit, withdrawal]
	 * @param accountId (pk)
	 * @return 전체 , 입금, 출금 / 내역 3가지 타입 반환 -> detail.jsp
	 */
	@Transactional
	public List<HistoryAccount> readHistoryByAccountId(String type, Integer accountId, int page, int size){
		List<HistoryAccount> list = new ArrayList<>();
		int limit = size;
		int offset = (page -1) * size;
		list = historyRepository.findByAccountIdAndTypeOfHistory(type, accountId, limit, offset);
		return list;
	}

	public int countHistoryByAccountIdAndType(String type, Integer accountId) {
		return historyRepository.countByAccountIdAndType(type, accountId);
	}
	
	
	// 0813 푸시
}
