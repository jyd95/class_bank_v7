package com.tenco.bank.service;

import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SignInDTO;
import com.tenco.bank.dto.SignUpDTO;
import com.tenco.bank.handler.exception.DataDeleveryException;
import com.tenco.bank.handler.exception.RedirectException;
import com.tenco.bank.repository.interfaces.UserRepository;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.utils.Define;

import lombok.RequiredArgsConstructor;

@Service // IoC의 대상(싱글톤 패턴으로 관리됨)
@RequiredArgsConstructor
public class UserService {
	
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * 회원 등록 서비스 기능
	 * 트랜젝션 처리
	 * @param dto
	 */
	@Transactional // 트랜젝션 처리는 반드시 습관화
	public void createUser(SignUpDTO dto) {

		int result = 0;
		
		System.out.println(dto.getMFile().getOriginalFilename());
		
		try {
			
			// 코드 추가 부분
			// 회원 가입 요청시 사용자가 던진 비밀번호 값을 암호화 처리 하여 저장해야 함.
			String hashPwd = passwordEncoder.encode(dto.getPassword());
			System.out.println("hashPwd : " + hashPwd);
			dto.setPassword(hashPwd);
			//result = userRepository.insert(dto.toUser());
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
		User userEntity = null;
		try {
			userEntity = userRepository.findByUsername(dto.getUsername());
		} catch (DataAccessException e) {
			throw new DataDeleveryException("잘못된 처리 입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			throw new RedirectException("알 수 없는 오류", HttpStatus.SERVICE_UNAVAILABLE);
		}
		if(userEntity == null) {
			throw new DataDeleveryException("존재하지 않는 아이디 입니다.", HttpStatus.BAD_REQUEST);
		}
		boolean isPwdMatched = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
		if(isPwdMatched == false) {
			throw new DataDeleveryException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
		}
		return userEntity;
	}
}
