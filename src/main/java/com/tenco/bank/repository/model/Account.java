package com.tenco.bank.repository.model;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.utils.Define;
import com.tenco.bank.utils.ValueFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Account extends ValueFormatter{
	private Integer id;
	private String number;
	private String password;
	private Long balance;
	private Integer userId;
	private Timestamp createdAt;
	
	// 각종 기능
	// 패스워드 체크
	public void checkPassword(String password) {
		if(!this.password.equals(password)) {
			throw new DataDeleveryException(Define.FAIL_ACCOUNT_PASSWROD, HttpStatus.BAD_REQUEST);
		}
	}
	// 잔액 여부 확인
	public void checkBalance(Long amount) {
		if(this.balance < amount) {
			throw new DataDeleveryException(Define.LACK_Of_BALANCE, HttpStatus.BAD_REQUEST);
		}
	}
	// 계좌 소유자 확인 등등
	public void checkOwner(Integer userId) {
		if(!this.userId.equals(userId)) {
			throw new DataDeleveryException(Define.NOT_ACCOUNT_OWNER, HttpStatus.BAD_REQUEST);
		}
	}
	
	// 출금
	public void withdraw(Long amount) {
		// 방어적 코드
		this.balance -= amount;
	}
	
	// 입금
	public void deposit(Long amount) {
		this.balance += amount;
	}
}
