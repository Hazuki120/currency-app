package com.example.exchange.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;
import com.example.exchange.infrastructure.api.CurrencyRateApiClient;

/**
 *  通貨レート取得・変換処理を担当するサービス
 *  
 *  ・外部 API からのレート取得
 *  ・レート履歴の保存
 *  ・最新レートの取得
 *  ・金額変換ロジック
 *  を提供する。
 */
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
	 * 
	 * @param base 基準通貨
	 * @param target 対象通k
	 * @return 最新レート
	 */
	public double fetchRateFromApi(String base, String target) {
		return apiClient.getRate(base, target);
	}

	/**
	 * レート履歴を DB に保存する
	 * 
	 * @param username ユーザ名
	 * @param base 基準通貨
	 * @param target 対象通貨
	 * @param rate レート
	 * @param amount 変換前の金額
	 * @return 保存されたエンティティ
	 */
	public CurrencyRate saveRate(
			String username,
			String base,
			String target,
			double rate,
			double amount) {
		BigDecimal bdAmount = BigDecimal.valueOf(amount);
		BigDecimal bdRate = BigDecimal.valueOf(rate);
		
		BigDecimal converted = bdAmount.multiply(bdRate).setScale(2, RoundingMode.HALF_UP);

		CurrencyRate entity = new CurrencyRate(
				username,
				base,
				target,
				rate,
				amount,
				converted.doubleValue(),	// ← BigDecimal → double に変換
				LocalDateTime.now()
		);
		
		return rateRepository.save(entity);
	}
	
	/**
	 * ユーザのレート履歴一覧を取得
	 * 
	 * @param username ユーザ名
	 * @return レート履歴リスト
	 */
	public List<CurrencyRate> getAllRates(String username){
		return rateRepository.findByUsernameOrderByFetchedAtDesc(username);
	}

	/**
	 * 指定したユーザ・通貨ペアの最新レートを取得
	 * 
	 * @param username ユーザ名
	 * @param base 基準通貨
	 * @param target 対象通貨
	 * @return 最新レート（存在しない場合は null ）
	 */
	public CurrencyRate getLatestRate(String username, String base, String target) {
		return rateRepository
				.findTopByUsernameAndBaseCurrencyAndTargetCurrencyOrderByFetchedAtDesc(username, base, target)
				.orElse(null);
	}

	/**
	 * 金額変換のメインロジック
	 * ・1時間以内なら DB のレートを使用
	 * ・1時間以上経過していれば API 再取得
	 * ・履歴は毎回保存
	 * 
	 * @return 変換後の金額
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
	 * レートが１時間以上前かどうか判定
	 * 
	 * @param rate レート履歴
	 * @return 有効期限切れなら true
	 */
	private boolean isExpired(CurrencyRate rate) {
		return rate.getFetchedAt()
				.isBefore(LocalDateTime.now().minusHours(1));
	}

}
