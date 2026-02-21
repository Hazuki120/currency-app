package com.example.exchange.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exchange.domain.model.CurrencyRate;

/**
 * 通貨レート履歴を操作するリポジトリ
 * 
 * Spring Data JPA の命名規則に基づき、
 * ユーザごとの履歴取得や最新レート取得を行う。
 */
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
	
	/**
	 * 指定ユーザのレート履歴一覧を取得（新しい順）
	 * 
	 * @param username ユーザ名
	 * @return レート履歴のリスト
	 */
	Page<CurrencyRate>findByUsernameOrderByFetchedAtDesc(String username, Pageable pageable);
	/** 
	 * 指定ユーザ・通貨ペアの最新レートを取得
	 * 
	 * @param username ユーザ名
	 * @param baseCurrency 基準通貨
	 * @param targetCurrency 対象通貨
	 * @return 最新レート（存在しない場合は empty ）
	 */
	Optional<CurrencyRate>
	findTopByUsernameAndBaseCurrencyAndTargetCurrencyOrderByFetchedAtDesc(
			String username,
			String baseCurrency,
			String targetCurrency);

}