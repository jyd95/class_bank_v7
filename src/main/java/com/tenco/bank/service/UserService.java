package com.tenco.bank.service;

import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SignInDTO;
import com.tenco.bank.dto.SignUpDTO;
import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.repository.interfaces.UserRepository;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.utils.Define;

@Service // IoC의 대상(싱글톤 패턴으로 관리됨)
public class UserService {

	private UserRepository userRepository;
	
//  @Autowired 어노테이션으로 대체 가능 하다.
//  생성자 의존 주입 - DI 	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * 회원 등록 서비스 기능
	 * 트랜젝션 처리
	 * @param dto
	 */
	@Transactional // 트랜젝션 처리는 반드시 습관화
	public void createUser(SignUpDTO dto) {

		int result = 0;

		try {
			result = userRepository.insert(dto.toUser());
		} catch (DataAccessException e) {
			throw new DataDeleveryException(Define.EXIST_NAME, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RedirectException(Define.UNKNOWN, HttpStatus.SERVICE_UNAVAILABLE);
		}
		if(result != 1) {
			throw new DataDeleveryException(Define.FAIL_TO_CREATE_USER, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public User readUser(SignInDTO dto) {
		// 유효성 검사는 Controller 에서 먼저 하자.
		User userEntity = null;
		try {
			userEntity = userRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
		} catch (DataAccessException e) {
			throw new DataDeleveryException("잘못된 처리 입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			throw new RedirectException("알 수 없는 오류", HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		if(userEntity == null) {
			throw new DataDeleveryException("아이디 혹은 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
		}
		
		
		return userEntity;
	}
}
