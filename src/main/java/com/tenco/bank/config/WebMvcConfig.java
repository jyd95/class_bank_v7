package com.tenco.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bank.handler.AuthInterceptor;

import lombok.RequiredArgsConstructor;

// @component => 한개의 클래스단위로 IoC 하고 싶다면
// @configuration => 한개 이상의 bean 을 IoC를 하고 싶다면
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Autowired // DI
	private final AuthInterceptor authInterceptor;
	
	// @RequiredArgsConstructor => 생성자 대신 사용할 수 있다.
	
	// 우리가 만들어 놓은 AuthInterceptor 를 등록해야 함. 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
				.addPathPatterns("/account/**")
				.addPathPatterns("/auth/**");
	}
	
	@Bean // IoC 대상 (싱글톤 처리)
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
