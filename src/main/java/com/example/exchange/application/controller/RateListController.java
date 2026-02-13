package com.example.exchange.application.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exchange.domain.service.CurrencyRateService;

/**
 * ログインユーザのレート履歴を表示するコントローラ
 * 
 * 認証済みユーザの username を取得し、
 * Service 層を通して該当ユーザの履歴のみ取得する。
 * 
 * Controller は「画面制御のみ」を担当し、
 * データ取得ロジックは Service に委譲している。
 */
@Controller
@RequestMapping("/rates")
public class RateListController {
	
	private final CurrencyRateService rateService;
	
	public RateListController(CurrencyRateService rateService) {
		this.rateService = rateService;
	}
	
	/**
	 * ログイン中のユーザの履歴一覧を取得し、
	 * rate-list.html に渡す
	 * 
	 * @param user ログイン中のユーザ情報
	 * @param model 画面へ値を渡すための Model
	 * @return 表示するテンプレート名
	 */
	@GetMapping
	public String showRateList (
		@AuthenticationPrincipal UserDetails user,
		Model model) {
		
		String username = user.getUsername();
		
		model.addAttribute("rates",
				rateService.getRatesByUsername(username));
		
		return "rate-list";
	}
}