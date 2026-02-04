package com.example.exchange.domain.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;
import com.example.exchange.infrastructure.api.ExchangeRateApiClient;

@Service
public class ExchangeService {
	private final ExchangeRateApiClient apiClient;
	private final CurrencyRateRepository rateRepository;
	
	public ExchangeService(CurrencyRateRepository rateRepository, ExchangeRateApiClient apiClient) {
		this.rateRepository = rateRepository;
		this.apiClient = apiClient;
	}
	
	// 外部APIからレートを取得する部分は後
	public double fetchRateFromApi(String base, String target) {
		// TODO : API クライアントを呼ぶ
		return apiClient.getRate(base, target);
	}
	
	// レートを DB に保存
	public CurrencyRate saveRate(String base, String target, double rate) {
		CurrencyRate entity = new CurrencyRate(
		base, target, rate, LocalDateTime.now()
		);
		return rateRepository.save(entity);
	}
	
	// 最新レートを取得
	public CurrencyRate getLatestRate(String base, String target) {
		return rateRepository.findTopByBaseCurrencyAndTargetCurrencyOrderByFetchedAtDesc(base, target).orElse(null);
	}
	// 金額を変換
	public double convert(double amount, String base, String target) {
		CurrencyRate latest = getLatestRate(base, target);
		// DB に無い場合は API から取得して保存
		if(latest == null) {
			double rate = fetchRateFromApi(base, target);
			latest = saveRate(base, target, rate);
		}
		
		// レートが1時間以上前なら更新
		LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
		if(latest.getFetchedAt().isBefore(oneHourAgo)) {
			double newRate = fetchRateFromApi(base, target);
			latest = saveRate(base, target, newRate);
		}
		
		return amount * latest.getRate();
	}
	
}
