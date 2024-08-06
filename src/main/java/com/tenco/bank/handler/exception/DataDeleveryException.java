package com.tenco.bank.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DataDeleveryException extends RuntimeException{
	
	private HttpStatus status;
	
	public DataDeleveryException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
}
