package com.example.exchange.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;

@Service
public class CurrencyRateService {

	private final CurrencyRateRepository repository;
	
	// コンストラクタインジェクション
	public CurrencyRateService(CurrencyRateRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * ログインユーザのレート履歴を取得
	 */
	public List<CurrencyRate> getRatesByUsername(String username){
		return repository.findByUsernameOrderByFetchedAtDesc(username);
		}
}
