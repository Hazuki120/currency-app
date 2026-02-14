package com.example.exchange.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン画面表示用コントローラ
 * 
 * 認証処理自体は Spring Security に委譲し、
 * 本コントローラは画面表示のみを担当する。
 */
@Controller
public class LoginController {

	/**
	 * ログイン画面を表示
	 * 
	 * @return 表示するテンプレート名
	 */
	@GetMapping("/login")
	public String login() {
		return "login";	// templates/login.html
	}
}
