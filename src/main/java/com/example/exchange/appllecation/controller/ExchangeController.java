package com.example.exchange.appllecation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.exchange.domain.service.ExchangeService;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {
	private final ExchangeService exchangeService;
	
	public ExchangeController(ExchangeService exchangeService) {
		this.exchangeService = exchangeService;
	}
	
	// 動作確認用:レート取得（まだAPI未実装なのでダミー
	@GetMapping("/rate")
	public double getRate(@RequestParam String base, @RequestParam String target) {
		// TODO : API クライアント実装後に fetchRateFromApi を呼ぶ
		return exchangeService.fetchRateFromApi(base, target);
	}
	// 最新レートを返す
	@GetMapping("/latest")
	public Object getLatest(@RequestParam String base, @RequestParam String target) {
		return exchangeService.getLatestRate(base, target);
	}
	//金額変換
	@GetMapping("/convert")
	public double convert(@RequestParam double amount, @RequestParam String base, @RequestParam String target) {
		return exchangeService.convert(amount, base, target);
	}
}
