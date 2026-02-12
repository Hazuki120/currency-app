package com.example.exchange.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン画面表示用コントローラ
 */
@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
