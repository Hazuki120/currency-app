package com.example.exchange.appllecation.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exchange.domain.service.CurrencyConversionService;

@Controller
public class CurrencyController {
	private final CurrencyConversionService currencyService;
	
	public CurrencyController(CurrencyConversionService currencyService) {
		this.currencyService = currencyService;
	}
	
	// レート取得
	@GetMapping("/save")
	public Object save(@AuthenticationPrincipal UserDetails user,@RequestParam String base, @RequestParam String target) {
		String username = user.getUsername();
		double rate = currencyService.fetchRateFromApi(base, target);
		return currencyService.saveRate(username, base, target, rate);
	}
	// 最新レートを返す
	@GetMapping("/latest")
	public Object getLatest(@RequestParam String base, @RequestParam String target) {
		return currencyService.getLatestRate(base, target);
	}
	//金額変換
	@GetMapping("/convert")
	public double convert(@AuthenticationPrincipal UserDetails user, @RequestParam double amount, @RequestParam String base, @RequestParam String target) {
		String username = user.getUsername();
		return currencyService.convert(username, amount, base, target);
	}
	@GetMapping("/exchange")
	public String showForm() {
		return "exchange"; 	// templates/exchange.html を表示
	}
	@GetMapping("/exchange/result")
	public String showResult(
			@AuthenticationPrincipal UserDetails user,
			@RequestParam double amount,
			@RequestParam String base,
			@RequestParam String target,
			Model model) {
		
		String username = user.getUsername();
		double result = currencyService.convert(username, amount, base, target);
		
		model.addAttribute("amount", amount);
		model.addAttribute("base", base);
		model.addAttribute("target", target);
		model.addAttribute("result", result);
		
		return "result";
	}
}
