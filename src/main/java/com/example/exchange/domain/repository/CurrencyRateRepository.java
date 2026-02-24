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
 * ・ユーザごとの履歴取得
 * ・最新レートの取得
 * ・論理削除を考慮した検索
 * を自動生成されたクエリで実現する。
 * 
 * Repository は DB アクセスのみを担当し、
 * ビジネスロジックは Service 層に委譲する。
 */
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
	
	/**
	 * 指定ユーザのレート履歴一覧を取得（新しい順）
	 * ・論理削除されていないデータのみ取得する（deleted = false）
	 * ・fetchedAt の降順で並べる
	 * ・ページング対応
	 * 
	 * @param username ユーザ名
	 * @param pageable ページング情報 
	 * @return レート履歴のページ
	 */	
	Page<CurrencyRate>findByUsernameAndDeletedFalseOrderByFetchedAtDesc(String username, Pageable pageable);
	/** 
	 * 指定ユーザ・通貨ペアの最新レートを取得する
	 * 
	 * ・fetchedAt の降順で最初の１件を取得（最新レコード）
	 * ・論理削除されたデータも対象（最新レート取得用途のため）
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