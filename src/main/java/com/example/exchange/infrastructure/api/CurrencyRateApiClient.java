package com.example.exchange.infrastructure.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 外部為替APIとの通信を担当するクライアントクラス（Infrastructuer層）
 * 
 * 責務
 * ・為替レート取得 API への HTTP リクエスト送信
 * ・API レスポンス（JSON）を DTO へ変換
 * 
 * 設計意図
 * ・外部サービスとの通信処理を Infrastructuer 層に閉じ込め、
 * 		Domain 層と分離することで責務を明確化している。
 * ・RestTemplate は Bean として定義し、コンストラクタインジェクションで受け取ることで
 * 		テスト容易性と依存性の明確化を実現している。
 */
@Component
public class CurrencyRateApiClient {

	/**
	 * Spring 管理の RestTemplate 
	 * 直接 new せず、Bean として注入することでテスト時にモックへ差し替え可能にしている
	 */
	private final RestTemplate restTemplate;

	@Value("${exchange.api.key}")
	private String apiKey;
	
	/**
	 * application.properties に定義した API キー。
	 * ソースコードへ直接書かず、外部設定から読み込むことで
	 * セキュリティと環境依存の文ふぃを実現している。
	 */
	public CurrencyRateApiClient(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}

	/**
	 * 指定された通貨ペアの為替レートを取得する。
	 * 
	 * 処理内容
	 * ・UriConponentsBuilder を使用し、安全に URL を構築
	 * ・API レスポンスを ExchangeResponse DTO へ自動変換
	 * ・レスポンスが不正な場合は例外をスロー
	 * 
	 * 設計ポイント
	 * ・数値計算の制度保持のため、戻り値は BigDecimal を採用
	 * ・JSON を直接扱わず DTO へマッピングすることで型安全性を確保
	 * 
	 * @param base 基準通貨
	 * @param target 変換先通貨
	 * @return 取得した為替レート
	 */
	public BigDecimal getRate(String base, String target) {

		String url = UriComponentsBuilder
				.fromUriString("https://api.exchangerate.host/convert")
				.queryParam("access_key", apiKey)
				.queryParam("from", base)
				.queryParam("to", target)
				.queryParam("amount", 1)
				.build()
				.toUriString();

		ExchangeResponse response = restTemplate.getForObject(url, ExchangeResponse.class);

		if (response == null || response.getResult() == null) {
			throw new IllegalStateException("API returned no result: " + response);
		}

		return response.getResult();
	}
}