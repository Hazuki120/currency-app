package com.example.exchange.appllecation.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.exchange.domain.User;
import com.example.exchange.domain.UserRepository;

@Controller
public class SignUpController {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SignUpController(UserRepository userRepository,
							PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/signup")
	public String showSignUpForm() {
		return "signup";
	}
	
	@PostMapping("/signup")
	public String registerUser(String username, String password) {
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));	// <- ハッシュ化
		user.setRole("USER");
		
		userRepository.save(user);
		
		return "redirect:/login?registered";
	}
}
