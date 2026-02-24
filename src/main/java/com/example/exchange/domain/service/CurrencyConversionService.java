package com.example.exchange.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;
import com.example.exchange.infrastructure.api.CurrencyRateApiClient;

/**
 *  通貨レート取得・変換処理を担当するサービス
 *  
 *  提供機能：
 *  ・外部 API からのレート取得
 *  ・レート履歴の保存
 *  ・最新レートの取得（１時間キャッシュ）
 *  ・金額変換ロジック
 *
 * Controller からビジネスロジックを切り離し、
 * 変換処理に関する責務をこのクラスに集約する
 */
@Service
public class CurrencyConversionService {
	
	/** 外部 API クライアント（為替レート取得用 */
	private final CurrencyRateApiClient apiClient;
	
	/** レート履歴を保存・取得するリポジトリ */
	private final CurrencyRateRepository rateRepository;

	/**
	 * コンストラクタインジェクション
	 * 依存関係を明確にし、テスト容易性を高めるために採用。
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
	 * @param target 対象通貨
	 * @return 最新レート
	 */
	public BigDecimal fetchRateFromApi(String base, String target) {
		return apiClient.getRate(base, target);
	}

	/**
	 * レート履歴を DB に保存する
	 * 
	 * ・レートは scale=4 金額は scale=2 に正規化して保存
	 * ・変換後金額も計算して保存
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
			BigDecimal rate,
			BigDecimal amount) {
		
		// DB に保存するレートを必ず同じ制度にするため↓
		BigDecimal normalizedRate = rate.setScale(4, RoundingMode.HALF_UP);
		BigDecimal normalizedAmount = amount.setScale(2, RoundingMode.HALF_UP);
		BigDecimal converted = normalizedAmount.multiply(normalizedRate).setScale(2, RoundingMode.HALF_UP);

		CurrencyRate entity = new CurrencyRate(
				username,
				base,
				target,
				normalizedRate,
				normalizedAmount,
				converted,
				LocalDateTime.now()
		);
		
		return rateRepository.save(entity);
	}
	
	/**
	 * 指定ユーザのレート履歴一覧を取得（論理削除を除外）
	 * 
	 * @param username ユーザ名
	 * @param page ページ番号
	 * @param size １ページあたりの件数
	 * @return レート履歴リスト
	 */
	public Page<CurrencyRate> getActiveRatesByUsername(String username, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("fetchedAt").descending());
		return rateRepository.findByUsernameAndDeletedFalseOrderByFetchedAtDesc(username, pageable);
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
	 * 
	 * ・1時間以内なら DB のレートを使用（キャッシュ）
	 * ・1時間以上経過していれば API 再取得
	 * ・履歴は毎回保存する
	 * 
	 * @return 変換後の金額
	 */
	public BigDecimal convert(
			String username,
			BigDecimal amount,
			String base,
			String target) {

		CurrencyRate latest = getLatestRate(username, base, target);
		BigDecimal rate;

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
	
	private CurrencyRate convertInternal(String username,
			BigDecimal amount, String base, String target) {
		
		CurrencyRate latest = getLatestRate(username, base, target);
		BigDecimal rate;
		
		if(latest == null || isExpired(latest)) {
			rate = fetchRateFromApi(base, target);
		}else {
			rate = latest.getRate();
		}
		
		return saveRate(username, base, target, rate, amount);
	}
	
	/**
	 * エンティティを返す変換処理。
	 * API や他サービスから利用される可能性を考慮したメソッド
	 */
	public CurrencyRate convertWithEntity(String username,
			BigDecimal amount, String base, String target) {
		return convertInternal(username, amount, base, target);
	}

}
