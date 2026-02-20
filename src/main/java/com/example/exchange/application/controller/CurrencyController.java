package com.example.exchange.application.controller;

import java.math.BigDecimal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.service.CurrencyConversionService;


/**
 * 通貨返還に関するリクエストを処理するコントローラ
 */
@Controller
public class CurrencyController {
	
	// 通貨変換サービス（DI）
	private final CurrencyConversionService currencyService;
	
	public CurrencyController(CurrencyConversionService currencyService) {
		this.currencyService = currencyService;
	}
	
	/**
	 * 最新レートを取得（API 用）
	 */
	@GetMapping("/latest")
	@ResponseBody
	public CurrencyRate getLatest(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam String base,
			@RequestParam String target) {
		
		String username = user.getUsername();
		return currencyService.getLatestRate(username, base, target);
	}
	
	/**
	 * 金額を変換する（API 用）
	 */
	@GetMapping("/convert")
	@ResponseBody
	public BigDecimal convert(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam BigDecimal amount,
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
			@RequestParam BigDecimal amount,
			@RequestParam String base,
			@RequestParam String target,
			Model model) {
		
		String username = user.getUsername();
		
		// 変換処理
		BigDecimal result = currencyService.convert(username, amount, base, target);
		
		// 画面へ値を渡す
		model.addAttribute("amount", amount);
		model.addAttribute("base", base);
		model.addAttribute("target", target);
		model.addAttribute("result", result);
		
		return "result";
	}
}