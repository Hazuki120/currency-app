package com.example.exchange.domain.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.User;
import com.example.exchange.domain.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * ユーザ名からユーザ情報を取得する
	 * Spring Security の認証時に自動で呼ばれる
	 */
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException{
		
		User user = userRepository.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException(
					"ユーザーが見つかりません:" + username);
		}
		
		// Sprint Security 用の UserDetails に変換して返す
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				.password(user.getPassword())
				// DB に "USER" を保存している場合はこちらでOK
				.roles(user.getRole())
				.build();
	}
}
