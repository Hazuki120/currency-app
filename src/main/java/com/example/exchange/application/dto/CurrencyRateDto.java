package com.example.exchange.application.dto;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * 通貨レート履歴の画面表示用 DTO
 * 
 * Entity を直接画面へ渡さないためのデータ転送オブジェクト
 */
@Getter
public class CurrencyRateDto {

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
	 */
	public CurrencyRateDto(
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
