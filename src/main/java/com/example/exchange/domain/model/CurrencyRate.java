package com.example.exchange.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 通貨レート履歴を保存するエンティティ
 * 1回の変換処理ごとに1レコード保存される
 */
@Entity
@Table(name = "currency_rate")
public class CurrencyRate {
	/** */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 主キー */
	@Column(nullable = false)
	private String username;
	
	/** 変換を行った */
	@Column(nullable = false)
	private String baseCurrency;
	
	/** 基準通貨(例：USD) */
	@Column(nullable = false)
	private String targetCurrency;
	
	/** 対象通貨(例：JPY) */
	@Column(nullable = false)
	private Double rate;
	
	/** 1単位当たりのレート（例：１USD = 153.6 JPY */
	@Column(nullable = false)
	private LocalDateTime fetchedAt;
	
	/** ユーザが変換した元の金額（例：100 USD) */
	@Column(nullable = false)
	private Double amount;
	
	/** 変換後の金額（例：100 USD → 15361 JPY） */
	@Column(nullable = false)
	private Double convertedAmount;
	
	/** レートを取得した日時(API を呼んだ時刻 or 最新レートの時刻) */
	public CurrencyRate() {}
	
	/**
	 * 通常のコンストラクタ
	 * @param username			ユーザー名
	 * @param baseCurrency		基準通貨
	 * @param targetCurrency	対象通貨
	 * @param rate				レート
	 * @param amount			変換した元の金額
	 * @param convertedAmount	返還後の金額
	 * @param fetchedAt			レート取得日時
	 */
	public CurrencyRate(String username, String baseCurrency, String targetCurrency, Double rate, Double amount, Double convertedAmount, LocalDateTime fetchedAt) {
		this.username = username;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.rate = rate;
		this.amount = amount;
		this.convertedAmount = convertedAmount;
		this.fetchedAt = fetchedAt;
	}
	
	// Getter(値を取り出すためのメソッド）
	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public String getTargetCurrency() {
		return targetCurrency;
	}
	public Double getRate() {
		return rate;
	}
	public LocalDateTime getFetchedAt() {
		return fetchedAt;
	}

}
