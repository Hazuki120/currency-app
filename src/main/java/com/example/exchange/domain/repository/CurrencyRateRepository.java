package com.example.exchange.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exchange.domain.model.CurrencyRate;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
	
	/** 指定ユーザのレート履歴一覧を取得 */
	List<CurrencyRate> findByUsernameOrderByFetchedAtDesc(String username);
	
	/** 指定ユーザの最新レートを取得 */
	Optional<CurrencyRate>
	findTopByUsernameAndBaseCurrencyAndTargetCurrencyOrderByFetchedAtDesc(
			String username,
			String baseCurrency,
			String targetCurrency);

}