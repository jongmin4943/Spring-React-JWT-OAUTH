package com.min.test.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.test.config.auth.PrincipalDetails;
import com.min.test.dto.LoginRequestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	
	// Authentication 객체 만들어서 리턴 -> 의존 : AuthenticationManager
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter 진입");
		
		//request에 있는 username과 password를 자바 Object로 받기(JSON)
		ObjectMapper om = new ObjectMapper();
		LoginRequestDto loginRequestDto = null;
		
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getUsername(),
						loginRequestDto.getPassword());
		System.out.println("JwtAuthenticationFilter 토큰 생성 완료");
		
		//authenticate() 함수가 호출되면 인증 프로바이더가 유저 디테일 서비스의
		//loadUserByUsername(토큰의 첫번째 parameter)를 호출
		//UserDetails를 리턴받아서 토큰의 두번째 parameter(credential)과
		//UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
		//Authentication 객체를 만들어서 필터체인으로 리턴해준다
		
		//인증 프로바이더의 디폴트 서비스는 UserDetailService타입
		//인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
		// 결론 : 인증 프로바이더에게 알려줄 필요가 없음.
		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);
		
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		return authentication;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
						.withSubject(principalDetails.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
						.withClaim("id", principalDetails.getUser().getId())
						.withClaim("username", principalDetails.getUser().getUsername())
						.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
}
