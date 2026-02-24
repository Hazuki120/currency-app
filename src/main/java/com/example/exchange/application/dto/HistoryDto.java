package com.example.exchange.application.dto;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * ログインユーザ向けの為替変換履歴画面で使用するDTO
 * 
 * Entity（CurrencyRate）を直接画面へ渡さず、
 * 表示に必要な情報だけを保持するためのデータ転送オブジェクト。
 * 
 * ・fetchedAt は画面表示用にフォーマット済み文字列として保持
 * ・論理削除された履歴は Service 層で除外されるため、この DTO には削除情報は含めない
 */
@Getter
public class HistoryDto {
	/** レコード ID */
	private Long id;
	/** 基準通貨 */
	private String baseCurrency;
	/** 対象通貨 */
	private String targetCurrency;
	/** 変換前金額 */
	private BigDecimal amount;
	/** 変換後金額 */
	private BigDecimal convertedAmount;
	/** レート */
	private String rate;
	/** 取得日時 */
	private String fetchedAtText;
	
	/**
	 * DTO コンストラクタ
	 * 
	 * Mapper（CurrencyRateMapper）からのみ呼び出される想定。
	 * 画面表示に必要な情報だけを受け取り保持する。
	 */
	public HistoryDto(
			Long id,
			String baseCurrency,
			String targetCurrency,
			BigDecimal amount,
			BigDecimal convertedAmount,
			String rate,
			String fetchedAtText) {
		this.id = id;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.amount = amount;
		this.convertedAmount = convertedAmount;
		this.rate = rate;
		this.fetchedAtText = fetchedAtText;
	}

}
