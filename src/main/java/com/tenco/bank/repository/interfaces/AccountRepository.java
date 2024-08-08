package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.bank.repository.model.Account;

@Mapper
public interface AccountRepository {
	public int insert(Account account);
	public int updateById(Account account);
	public int deleteById(Integer id);
	
	// 인터페이스 파라미터 명과 XML 에 사용할 변수명을 다르게 사용 한다면
	// @Param 애노테이션을 사용 해야 한다.
	// 2개 이상의 파라미터를 사용 할 때에도 반드시 사용! 
	public List<Account> findByUserId(@Param("userId") Integer principalId);
	
	// account id 값으로 계좌 정보 조회
	public Account findByNumber(@Param("number") String id);
	
	
}
