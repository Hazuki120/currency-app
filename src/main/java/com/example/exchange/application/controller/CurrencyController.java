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
 * 通貨変換に関するリクエストを処理するコントローラ
 */
@Controller
public class CurrencyController {
	
	private String username(UserDetails user) {
		return user.getUsername();
	}
	
	/** 通貨変換に関する業務処理を担当する */
	private final CurrencyConversionService currencyService;
	
	public CurrencyController(CurrencyConversionService currencyService) {
		this.currencyService = currencyService;
	}
	
	/**
	 * 最新レートを取得する API
	 * ログインユーザに紐づく最新レートを取得する。
	 * 
	 * @param user ログインユーザ
	 * @param base 基準通貨
	 * @param target 対象通貨
	 * @return 最新の通貨レート
	 */
	@GetMapping("/latest")
	@ResponseBody
	public CurrencyRate getLatest(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam String base,
			@RequestParam String target) {
		
		return currencyService.getLatestRate(username(user), base, target);
	}
	
	/**
	 * 金額を通貨変換する API
	 * ログインユーザに紐づくレートを使用して変換する。
	 * 
	 * @param user ログインユーザ
	 * @param amount 変換金額
	 * @param base 基準通貨
	 * @param target 対象通貨
	 * @return 変換後金額
	 */
	@GetMapping("/convert")
	@ResponseBody
	public BigDecimal convert(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam BigDecimal amount,
			@RequestParam String base,
			@RequestParam String target) {

		return currencyService.convert(username(user), amount, base, target);
	}
	
	/**
	 * 通貨変換フォーム画面表示
	 *  
	 * @return 通貨変換フォームテンプレート
	 */
	@GetMapping("/exchange")
	public String showForm() {
		return "exchange"; 	// templates/exchange.html を表示
	}
	
	/**
	 * 通貨変換結果画面を表示する。
	 * リクエストパラメータを元に再度変換処理を実行し、
	 * 結果を画面へ表示する
	 * 
	 * @param user ログインユーザ
	 * @param amount 変換金額
	 * @param base 基準通貨
	 * @param target 対象通貨
	 * @param model 画面へ値を渡す
	 * @return 結果画面テンプレート
	 */
	@GetMapping("/exchange/result")
	public String showResult(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam BigDecimal amount,
			@RequestParam String base,
			@RequestParam String target,
			Model model) {
		
		// 変換処理
		BigDecimal result = currencyService.convert(username(user), amount, base, target);
		
		// 画面へ値を渡す
		model.addAttribute("amount", amount);
		model.addAttribute("base", base);
		model.addAttribute("target", target);
		model.addAttribute("result", result);
		
		return "result";
	}
}