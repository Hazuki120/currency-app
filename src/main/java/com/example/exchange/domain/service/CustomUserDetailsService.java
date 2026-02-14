package com.example.exchange.domain.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.User;
import com.example.exchange.domain.repository.UserRepository;

/**
 * Spring Security の認証処理で使用される UserDetailsService の実装。
 * 
 * ユーザ名からユーザ情報を取得し、
 * 認証に必要な UserDetails オブジェクトを生成して返す。
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	/** コンストラクタインジェクション **/
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * ユーザ名からユーザ情報を取得する。
	 * Spring Security の認証時に自動で呼び出される。
	 * 
	 * @param username 入力されたユーザ名
	 * @return UserDetails 認証用ユーザ情報
	 * @throws UsernameNotFoundException ユーザが存在しない場合
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
				// DB に "USER" を保存している場合は roles() で OK
				.roles(user.getRole())
				.build();
	}
}
