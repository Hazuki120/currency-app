package com.example.exchange.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.exchange.application.dto.CurrencyRateDto;
import com.example.exchange.application.mapper.CurrencyRateMapper;
import com.example.exchange.domain.model.CurrencyRate;
import com.example.exchange.domain.repository.CurrencyRateRepository;

/**
 * 通貨レート履歴の取得を担当するサービス
 * 
 * このクラスはビジネスロジック層として、
 * Repository からデータを取得する
 * Entity → DTO へ変換する
 * 
 * Controller は DTO のみ扱う設計とし、Entity を外部へ直接公開しない。
 */
@Service
public class CurrencyRateService {

	/** 通貨レートデータアクセスを担当する */
	private final CurrencyRateRepository rateRepository;
	
	private final CurrencyRateMapper mapper;
	
	/** コンストラクタインジェクション */
	public CurrencyRateService(CurrencyRateRepository repository, CurrencyRateMapper mapper) {
		this.rateRepository = repository;
		this.mapper = mapper;
	}
	
	/**
	 * 指定したユーザのレート履歴を取得（新しい順）
	 * 
	 * @param username ユーザ名
	 * @param page ページ番号
	 * @param size 件数
	 * @return Entity ページ
	 */
	public Page<CurrencyRate> getRatesByUsername(String username, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("fetchedAt").descending());
		
		return rateRepository.findByUsernameOrderByFetchedAtDesc(username, pageable);
		}
	
	/**
	 * 指定ユーザのレート履歴を DTO で取得
	 * 
	 * @param username ユーザ名
	 * @param page ページ番号
	 * @param size 件数
	 * @return DTO ページ
	 */
	public Page<CurrencyRateDto> getRatesDto(String username, int page, int size){
		Page<CurrencyRate> entityPage = getRatesByUsername(username, page, size);
		
		return entityPage.map(mapper::toDto);
		}

	/**
	 * 全ユーザのレート履歴を取得（管理者用）
	 * 
	 * @param pageable ページ情報
	 * @return 全レート
	 */
	public Page<CurrencyRateDto> findAllDto(Pageable pageable){
		return rateRepository.findAll(pageable).map(mapper::toDto);
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
