package com.example.exchange.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

/**
 * 管理者画面・ユーザ画面で使用する通貨レート履歴の DTO
 * 
 * Entity（CurrencyRate） を直接画面へ渡さず、
 * 必要な情報だけを抽出して保持するためのデータ転送オブジェクト。
 * 
 * ・画面表示用に fetchedAt は文字列（fetchedAtText）として保持
 * ・論理削除の状態（delete / deletedAt / deletedBy）も保持
 * ・管理者画面では username も表示するため DTO に含める
 */
@Getter
public class CurrencyRateDto {

	/** レコード ID */
	private Long id;
	/** レートを登録したユーザ名 */
	private String username;
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
	/** 論理削除フラグ */
	private boolean deleted;
	/** 論理削除日時 */
	private LocalDateTime deletedAt;
	/** 削除を実行したユーザ名 */
	private String deletedBy;
	
	/**
	 * DTO コンストラクタ
	 * 
	 * Mapper（CurrencyRateMapper）からのみ呼び出される想定。
	 * 画面表示に必要な情報だけを受け取り保持する。
	 */
	public CurrencyRateDto(
			Long id,
			String username,
			String baseCurrency,
			String targetCurrency,
			BigDecimal amount,
			BigDecimal convertedAmount,
			String rate,
			String fetchedAtText,
			boolean deleted,
			LocalDateTime deletedAt,
			String deletedBy) {
		this.id = id;
		this.username = username;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.amount = amount;
		this.convertedAmount = convertedAmount;
		this.rate = rate;
		this.fetchedAtText = fetchedAtText;
		this.deleted = deleted;
		this.deletedAt = deletedAt;
		this.deletedBy = deletedBy;
	}
}
