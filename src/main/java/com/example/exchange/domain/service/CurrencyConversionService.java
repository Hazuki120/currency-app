package com.example.exchange.domain.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;
import com.example.exchange.infrastructure.api.CurrencyRateApiClient;

@Service
public class CurrencyConversionService {
	
	private final CurrencyRateApiClient apiClient;
	private final CurrencyRateRepository rateRepository;

	/**
	 * コンストラクタインジェクション
	 */
	public CurrencyConversionService(
			CurrencyRateRepository rateRepository,
			CurrencyRateApiClient apiClient) {
		this.rateRepository = rateRepository;
		this.apiClient = apiClient;
	}

	/**
	 * 外部 API から最新レートを取得する
	 */
	public double fetchRateFromApi(String base, String target) {
		return apiClient.getRate(base, target);
	}

	/**
	 * レート履歴を DB に保存する
	 */
	public CurrencyRate saveRate(
			String username,
			String base,
			String target,
			double rate,
			double amount) {
		double converted = amount * rate;

		CurrencyRate entity = new CurrencyRate(
				username,
				base,
				target,
				rate,
				amount,
				converted,
				LocalDateTime.now()
		);
		
		return rateRepository.save(entity);
	}

	/**
	 * 全ユーザ共通の最新レートを取得
	 */
	public CurrencyRate getLatestRate(String base, String target) {
		return rateRepository
				.findTopByBaseCurrencyAndTargetCurrencyOrderByFetchedAtDesc(base, target)
				.orElse(null);
	}

	/**
	 * 金額変換のメインロジック
	 * ・1時間以内なら DB のレートを使用
	 * ・1時間以上経過していれば API 再取得
	 * ・履歴は毎回保存
	 */
	public double convert(
			String username,
			double amount,
			String base,
			String target) {

		CurrencyRate latest = getLatestRate(username, base, target);
		double rate;

		// DB レートが存在しない or 1時間以上経過
		if (latest == null || isExpired(latest)) {
			// 初回は API から取得
			rate = fetchRateFromApi(base, target);
		} else {
			rate = latest.getRate();
		}

		// 履歴は毎回保存
		CurrencyRate saved = saveRate(username, base, target, rate, amount);
		
		return saved.getConvertedAmount();
	}
	
	/**
	 * レートが 1時間以上前かどうか判定
	 */
	private boolean isExpired(CurrencyRate rate) {
		return rate.getFetchedAt()
				.isBefore(LocalDateTime.now().minusHours(1));
	}

}
