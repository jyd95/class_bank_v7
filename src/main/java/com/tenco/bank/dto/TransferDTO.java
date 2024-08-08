package com.tenco.bank.dto;

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

public class TransferDTO {
	private Long amount;			// 거래금액
	private String wAccountNumber;	// 입금 계좌번호
	private String dAccountNumber;	// 출금 계좌번호
	private String password;		// 출금 비밀번호
}
