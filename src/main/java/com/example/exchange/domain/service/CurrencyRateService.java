package com.example.exchange.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;

/**
 * 通貨レート履歴の取得を担当するサービス
 * 
 * Controller からの要求に応じて、
 * ユーザごとの履歴取得処理を Repository に委譲する。
 */
@Service
public class CurrencyRateService {

	private final CurrencyRateRepository repository;
	
	/** コンストラクタインジェクション */
	public CurrencyRateService(CurrencyRateRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * 指定したユーザのレート履歴を取得（新しい順）
	 * 
	 * @param username ユーザ名
	 * @return レート履歴リスト
	 */
	public List<CurrencyRate> getRatesByUsername(String username){
		return repository.findByUsernameOrderByFetchedAtDesc(username);
		}
}
