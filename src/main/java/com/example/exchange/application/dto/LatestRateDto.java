package com.example.exchange.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 最新の通貨レート情報を画面や API に返すための DTO
 * 
 * ・Entity（CurrencyRate）を直接返さず、必要な情報だけを保持する。
 * ・最新レート表示では金額や削除情報は不要なため、この DTO は
 * 		baseCurrency / targetCurrency / rate / fetchedAtText のみに絞っている。
 * ・fetchedAtText は画面表示用にフォーマット済みの文字列。
 */
@Getter
@AllArgsConstructor
public class LatestRateDto {
	
	/** 基準通貨 */
	private String baseCurrency;
	/** 対象通貨 */
	private String targetCurrency;
	/** レート */
	private String rate;
	/** 取得日時 */
	private String fetchedAtText;

}
