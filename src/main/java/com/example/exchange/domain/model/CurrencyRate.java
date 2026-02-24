package com.example.exchange.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 通貨レート履歴を保存するエンティティ
 * 
 *・1回の変換処理につき1レコード保存される。
 *・ユーザが行った変換内容（通貨、金額、レート、日時）を保持する。
 *・論理削除（deleted / deletedAt / deletedBy）に対応し、
 *	履歴の復元性と監査制を確保する。
 */
@Entity
@Table(name = "currency_rate")
@Getter
@Setter
public class CurrencyRate {
	/** 主キー（自動採番）  */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 変換を行ったユーザ名 */
	@Column(nullable = false)
	private String username;
	
	/** 基準通貨(例：USD) */
	@Column(nullable = false)
	private String baseCurrency;
	
	/** 対象通貨(例：JPY) */
	@Column(nullable = false)
	private String targetCurrency;
	
	/** 1単位当たりのレート（例：１USD = 153.6 JPY) */
	@Column(nullable = false, precision = 18, scale = 4)
	private BigDecimal rate;
	
	/** レートを取得した日時（API呼び出し時刻） */
	@Column(nullable = false)
	private LocalDateTime fetchedAt;
	
	/** ユーザが変換した元の金額（例：100 USD) */
	@Column(nullable = false, precision = 18, scale = 2)
	private BigDecimal amount;
	
	/**  変換後の金額（例：100 USD → 15361 JPY） */
	@Column(nullable = false, precision = 18, scale = 2)
	private BigDecimal convertedAmount;
	
	/** 論理削除フラグ（true = 削除済み） */
	@Column(nullable = false)
	private boolean deleted = false;
	
	/** 論理削除日時（null の場合は未削除） */
	@Column
	private LocalDateTime deletedAt;
	
	/** 削除を実行したユーザ名（ADMIN または一般ユーザ） */

	@Column
	private String deletedBy;
	
	/** JPA 用のデフォルトコンストラクタ */
	public CurrencyRate() {}
	
	/**
	 * 通常のコンストラクタ
	 * 変換処理時に必要な情報をまとめて設定する。
	 * 
	 * @param username			ユーザー名
	 * @param baseCurrency		基準通貨
	 * @param targetCurrency	対象通貨
	 * @param rate				レート
	 * @param amount			変換前の金額
	 * @param convertedAmount	変換後の金額
	 * @param fetchedAt			レート取得日時
	 */
	public CurrencyRate(
			String username,
			String baseCurrency,
			String targetCurrency,
			BigDecimal rate,
			BigDecimal amount,
			BigDecimal convertedAmount,
			LocalDateTime fetchedAt) {
		
		this.username = username;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.rate = rate;
		this.amount = amount;
		this.convertedAmount = convertedAmount;
		this.fetchedAt = fetchedAt;
	}
	
	/**
	 * 論理削除フラグを設定する。
	 * （Lombok の setter を使わず、意図を明確にするために明示的に定義）
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
