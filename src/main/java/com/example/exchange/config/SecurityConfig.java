package com.example.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security の設定クラス
 * 
 * ・URL ごとのアクセス制御
 * ・ログイン/ログアウト設定
 * ・パスワードのハッシュ化方式
 * を定義している
 */
@Configuration
public class SecurityConfig {

	/**
	 * セキュリティフィルタチェーンの設定
	 * 
	 * HttpSecurity を使って、
	 * ・どの URL を許可するか
	 * ・ログイン成功後の遷移先
	 * ・ログアウト後の遷移先
	 * を定義する
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// ----------------------------
				// ① URL ごとのアクセス制御
				// ----------------------------
				.authorizeHttpRequests(auth -> auth
						// ログイン・サインアップ画面は未承認でもアクセス可能
						.requestMatchers("/login", "/signup").permitAll()
						// それ以外の URL はログイン必須
						.anyRequest().authenticated()
				)
				
				// ------------------------
				// ②フォームログイン設定
				// ------------------------
				.formLogin(login -> login
						// 独自ログインページを使用
						.loginPage("/login")
						// ログイン成功時は 常に /exchange へ遷移
						// （true を外すと「元のページに戻る」挙動になる）
						.defaultSuccessUrl("/exchange", true)
						.permitAll()
				)
				
				// -------------------
				// ③ログアウト設定
				// -------------------
				.logout(logout -> logout
						// ログアウト後はログイン画面へリダイレクト
						.logoutSuccessUrl("/login?logout")
						.permitAll()
				);
		
		return http.build();
	}

	/**
	 * パスワードエンコーダーの設定
	 * 
	 * BCrypt を使用してパスワードをハッシュ化する。
	 * 平文保存を防ぎ、セキュリティを強化する。
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
