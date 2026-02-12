package com.example.exchange.application.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.domain.service.CurrencyConversionService;

/**
 * 通貨返還に関するリクエストを処理するコントローラ
 */
@Controller
public class CurrencyController {
	
	// サービス層を DI （依存性注入）
	private final CurrencyConversionService currencyService;
	
	public CurrencyController(CurrencyConversionService currencyService) {
		this.currencyService = currencyService;
	}
	
	/**
	 * 外部 API からレートを取得し、DBに保存する
	 */
	@PostMapping("/save")
	public String save(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam String base, 
			@RequestParam String target, 
			@RequestParam double amount) {
		
		String username = user.getUsername();
		
		// 外部 API からレートを取得
		double rate = currencyService.fetchRateFromApi(base, target);
		
		// DB へ保存
		currencyService.fetchRateFromApi(base, target);
		
		return "redirect:/exchange";
	}
	
	/**
	 * 最新レートを取得
	 */
	@GetMapping("/latest")
	@ResponseBody
	public Object getLatest(
			@RequestParam String base,
			@RequestParam String target) {
		
		return currencyService.getLatestRate(base, target);
	}
	
	/**
	 * 金額を変換する（API 用）
	 */
	@GetMapping("/convert")
	@ResponseBody
	public double convert(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam double amount,
			@RequestParam String base,
			@RequestParam String target) {
		
		String username = user.getUsername();
		return currencyService.convert(username, amount, base, target);
	}
	
	/**
	 * 通貨変換フォーム画面表示
	 */
	@GetMapping("/exchange")
	public String showForm() {
		return "exchange"; 	// templates/exchange.html を表示
	}
	
	/**
	 * 通貨変換結果画面表示
	 */
	@GetMapping("/exchange/result")
	public String showResult(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam double amount,
			@RequestParam String base,
			@RequestParam String target,
			Model model) {
		
		String username = user.getUsername();
		// 変換処理
		double result = currencyService.convert(username, amount, base, target);
		
		// 画面へ値を渡す
		model.addAttribute("amount", amount);
		model.addAttribute("base", base);
		model.addAttribute("target", target);
		model.addAttribute("result", result);
		
		return "result";
	}
}
