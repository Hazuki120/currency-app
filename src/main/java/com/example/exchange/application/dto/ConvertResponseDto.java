package com.example.exchange.application.dto;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * 通貨変換APIのレスポンスDTO
 * 
 * Controllerがクライアント（画面/JS/API）へ返すデータ構造
 */
@Getter
public class ConvertResponseDto {
	
	/** 変換前の金額 */
	private BigDecimal amount;
	/** 基準通貨 */
	private String baseCurrency;
	/** 対象通貨 */
	private String targetCurrency;
	/** 変換後金額 */
	private BigDecimal convertedAmount;
	/** 表示用レート */
	private String rate;
	/** 取得日時 */
	private String fetchedAtText;
	
	/**
	 * コンストラクタ
	 */
	public ConvertResponseDto(
			BigDecimal amount,
			String baseCurrency,
			String targetCurrency,
			BigDecimal convertedAmount,
			String rate,
			String fetchedAtText) {
		this.amount = amount;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.convertedAmount = convertedAmount;
		this.rate = rate;
		this.fetchedAtText = fetchedAtText;
	}

}
