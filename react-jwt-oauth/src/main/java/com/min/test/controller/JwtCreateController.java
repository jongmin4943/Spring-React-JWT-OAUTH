package com.min.test.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.min.test.config.jwt.JwtProperties;
import com.min.test.config.oauth.GoogleUser;
import com.min.test.config.oauth.OAuthUserInfo;
import com.min.test.model.User;
import com.min.test.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JwtCreateController {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	
	@PostMapping("/oauth/jwt/google")
	public String jwtCreate(@RequestBody Map<String,Object> data) {
		System.out.println("jwt생성 실행");
		System.out.println(data.get("profileObj"));
		OAuthUserInfo googleUser = 
				new GoogleUser((Map<String, Object>) data.get("profileObj"));
		
		User userEntity = userRepository.findByUsername(googleUser.getProvider()+"_"+googleUser.getProviderId());
		
		//없으면 가입시킴
		if(userEntity == null) {
			User userRequest = User.builder()
							.username(googleUser.getProvider()+"_"+googleUser.getProviderId())
							.password(encoder.encode("코봉밥"))
							.userRealname(googleUser.getName())
							.email(googleUser.getEmail())
							.provider(googleUser.getProvider())
							.providerId(googleUser.getProviderId())
							.roles("ROLE_USER")
							.build();
			userEntity = userRepository.save(userRequest);
		}
		
		String jwtToken = JWT.create()
						.withSubject(userEntity.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
						.withClaim("id", userEntity.getId())
						.withClaim("username", userEntity.getUsername())
						.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		return jwtToken;
	}

}
