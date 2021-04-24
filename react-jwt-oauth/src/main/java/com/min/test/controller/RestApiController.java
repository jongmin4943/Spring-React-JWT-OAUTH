package com.min.test.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.min.test.config.auth.PrincipalDetails;
import com.min.test.model.User;
import com.min.test.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestApiController {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	
	//모든 사람이 접근가능
	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
	}
	
	// JWT를 사용하면 UserDetailsService를 호출하지 않기때문에 @AuthenticationPrincipal사용 불가
	// 왜냐하면 UserDetailsService에서 리턴되야 만들어지기 때문이다.
	
	@GetMapping("user")
	public String user(Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("principalId : "+principal.getUser().getId());
		System.out.println("principalUsername : "+principal.getUser().getUsername());
		System.out.println("principalPassword : "+principal.getUser().getPassword());
		System.out.println("principalEmail : "+principal.getUser().getEmail());
		System.out.println("principalRoles : "+principal.getUser().getRoles());
		return "user";
	}
	
	// 매니저 혹은 어드민이 접근 가능
	@GetMapping("manager/reports")
	public String reports() {
		return "reports";
	}
	
	// 어드민이 접근 가능
	@GetMapping("admin/users")
	public List<User> users(){
		return userRepository.findAll();
	}
	
	@PostMapping("join")
	public String join(@RequestBody User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return "회원가입완료";
	}
}
