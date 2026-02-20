package com.example.exchange.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;

/**
 * 通貨レート履歴の取得を担当するサービス
 * 
 * Controller からの要求に応じて、
 * データ取得・削除などの処理をリポシトリへ委譲する。
 * 
 * 本クラスはビジネスロジック層として位置付けており、
 * コントローラにはデータアクセス処理を直接記述しない設計とすることで、
 * 責務分離を意識している。
 */
@Service
public class CurrencyRateService {

	/** 通貨レートデータアクセスを担当する */
	private final CurrencyRateRepository rateRepository;
	
	/** コンストラクタインジェクション */
	public CurrencyRateService(CurrencyRateRepository repository) {
		this.rateRepository = repository;
	}
	
	/**
	 * 指定したユーザのレート履歴を取得（新しい順）
	 * 
	 * @param username ユーザ名
	 * @return レート履歴リスト
	 */
	public Page<CurrencyRate> getRatesByUsername(String username, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("fetchedAt").descending());
		
		return rateRepository.findByUsernameOrderByFetchedAtDesc(username, pageable);
		}

	/**
	 * 全ユーザのレート履歴を取得する。
	 * 主に管理者画面で使用する。
	 * 
	 * @return 全レート一覧
	 */
	public List<CurrencyRate> findAll(){
		return rateRepository.findAll();
	}
	
	/**
	 * 指定 ID のレート情報を削除。
	 * 削除可否の業務ルールが追加された場合は
	 * 本サービス層に実装することを想定している。
	 * 
	 * @param id 削除対象レート ID
	 */
	public void deleteById(Long id) {
		rateRepository.deleteById(id);
	}
}
